{ pkgs ? import <nixpkgs> { }, lib ? pkgs.stdenv.lib }:

pkgs.mkShell rec {
  name = "quil-env";
  buildInputs = with pkgs; [
    xorg_sys_opengl
  ];
  LD_LIBRARY_PATH = "${lib.makeLibraryPath buildInputs}";
}
