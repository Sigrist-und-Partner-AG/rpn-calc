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
        version = "1.0.0";
        src = self;

        mvnJdk = pkgs.jdk11;
        mvnParameters = "-B";

        inherit SOURCE_DATE_EPOCH;
      };
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = with pkgs; [
          jdk11
          maven
        ];
        inherit LD_LIBRARY_PATH GTK_PATH SOURCE_DATE_EPOCH;
      };

      checks.${system}.default = pkgs.maven.buildMavenPackage (
        mvnDefaults
        // {
          pname = "${mvnDefaults.pname}-check";

          mvnHash = "sha256-4JmHuFaHPQrcfQH98eZELwGViD/N3/stMlk4EnLnr2k=";
          mvnParameters = "${mvnDefaults.mvnParameters} verify javadoc:javadoc";

          installPhase = ''touch "$out"'';
        }
      );

      packages.${system} = {

        javadocs = pkgs.maven.buildMavenPackage (
          mvnDefaults
          // {
            pname = "${mvnDefaults.pname}-javadocs";

            mvnHash = "sha256-4JmHuFaHPQrcfQH98eZELwGViD/N3/stMlk4EnLnr2k=";
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
            mvnHash = "sha256-FJ65l0+dwYJ1hqBVjpw0zTKXHXTe+SHWVqzICKvP1UA=";
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
