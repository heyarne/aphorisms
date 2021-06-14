{ pkgs ? import <nixpkgs> { }, }:

let
  lib = pkgs.lib;
in pkgs.mkShell rec {
  name = "quil-env";
  buildInputs = with pkgs; [
    xorg_sys_opengl
    clojure
  ];
  LD_LIBRARY_PATH = "${lib.makeLibraryPath buildInputs}";

  # we need to make sure the library is on the path for JOGL;
  # also, there's a bug that is avoided with the second config line
  # https://github.com/processing/processing/issues/5476
  JAVA_OPTS = "-Djava.library.path=${lib.makeLibraryPath buildInputs}"; 
    # -Djogl.disable.openglcore=\"true\"
  # LIBGL_ALWAYS_SOFTWARE = true;
  # JAVA_LIBRARY_PATH = "${lib.makeLibraryPath buildInputs}";
}
