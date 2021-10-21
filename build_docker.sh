version=$1

if [ -z $version ]
then
 echo "usage: $0 <version>"
 exit 1
fi

jarfile=target/pulse-energy-${version}.jar

imagename=daniu/pulse-energy:$version
docker build --build-arg JARFILE=${jarfile} -t $imagename -f docker/Dockerfile .
docker tag $imagename daniu/pulse-energy:${version}-armv7
