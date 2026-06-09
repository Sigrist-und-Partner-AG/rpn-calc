{
  description = "RPN Calculator";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
  };

  outputs =
    { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
      inherit (pkgs) lib;

      # Runtime dependencies required by JavaFX
      LD_LIBRARY_PATH = lib.makeLibraryPath (
        with pkgs;
        [
          glib
          gtk3
          libcanberra-gtk3
          libGL
          libxtst
          mesa
        ]
      );
      GTK_PATH = "${pkgs.libcanberra-gtk3}/lib/gtk-3.0";

      # Earliest valid timestamp for Javadocs
      SOURCE_DATE_EPOCH = "315532802";

      # Common Maven settings
      mvnDefaults = {
        pname = "rpn-calc";
        version = "1.1.0";
        src = self;

        # JDK 11's jlink can produce non-reproducible runtime images:
        # "$out/lib/modules" differs between otherwise identical builds.
        # Make Nix use JDK 17 while retaining Java 11 compatibility.
        mvnJdk = pkgs.jdk17;
        mvnParameters = "-B";

        inherit SOURCE_DATE_EPOCH;
      };
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = [ mvnDefaults.mvnJdk pkgs.maven ];
        inherit LD_LIBRARY_PATH GTK_PATH SOURCE_DATE_EPOCH;
      };

      checks.${system}.default = pkgs.maven.buildMavenPackage (
        mvnDefaults
        // {
          pname = "${mvnDefaults.pname}-check";

          mvnHash = "sha256-K8sNvP1gzbhpWnFA6kYxldAUXo6jInQmvpGJeOsrN1M=";
          mvnParameters = "${mvnDefaults.mvnParameters} verify javadoc:javadoc";

          installPhase = ''touch "$out"'';
        }
      );

      packages.${system} = {

        javadocs = pkgs.maven.buildMavenPackage (
          mvnDefaults
          // {
            pname = "${mvnDefaults.pname}-javadocs";

            mvnHash = "sha256-K8sNvP1gzbhpWnFA6kYxldAUXo6jInQmvpGJeOsrN1M=";
            mvnParameters = "${mvnDefaults.mvnParameters} javadoc:javadoc";

            installPhase = ''
              mkdir -p "$out/"
              cp -R target/reports/apidocs/. "$out/"
            '';
          }
        );

        default = pkgs.maven.buildMavenPackage (
          mvnDefaults
          // {
            mvnHash = "sha256-I2o7mEk028Fnz0o7Rozat9rO7vHOfhKw9f8AGCzrACQ=";
            mvnParameters = "${mvnDefaults.mvnParameters} javafx:jlink";

            nativeBuildInputs = [ pkgs.makeWrapper ];
            installPhase = ''
              mkdir -p "$out/img/"
              cp img/screenshot.png "$out/img/"
              cp README.md LICENSE "$out/"
              cp -R target/jlink-image/. "$out/"

              wrapProgram "$out/bin/rpn-calc" \
                --prefix PATH : "${lib.makeBinPath [ pkgs.coreutils ]}" \
                --prefix LD_LIBRARY_PATH : "${LD_LIBRARY_PATH}" \
                --prefix GTK_PATH : "${GTK_PATH}"
            '';

            meta = {
              description = "A JavaFX calculator for evaluating expressions in Reverse Polish Notation";
              homepage = "https://github.com/Sigrist-und-Partner-AG/rpn-calc";
              license = lib.licenses.mit;
              mainProgram = "rpn-calc";
              platforms = [ system ];
            };
          }
        );
      };

      apps.${system}.default = {
        type = "app";
        program = lib.getExe self.packages.${system}.default;
        meta.description = "Run the RPN calculator";
      };
    };
}
