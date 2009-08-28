VER=$1
echo "Will use version '${VER}'"
echo "Changing pom.xml files"
find . -name "pom.xml" |xargs perl version.pl ${VER}

