{
  description = "RPN Calculator";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs?ref=nixos-25.11-small";
  };

  outputs =
    { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = [
          pkgs.jdk11
          pkgs.maven
        ];

        # Runtime dependencies required by JavaFX
        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath [
          pkgs.glib
          pkgs.gtk3
          pkgs.libGL
          pkgs.xorg.libXtst
        ];
      };
    };
}
