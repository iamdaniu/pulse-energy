mvn clean package

version=$(ls target/pulse-energy*jar | sed 's#.*\-\(.*\)\.jar#\1#')

echo build version $version

./build_docker.sh $version
