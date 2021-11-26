#!/bin/sh

clear
## Menu Select Boxes ##
BOX_1="dm900-original"
BOX_2="dm900-clone"
BOX_3="dm920-original"

list=
for i in $(seq 1 3); do
    p="BOX_$i"
    list="$list $i ${!p} "
done
list=($list) #00ff2525
box=$(dialog --stdout --clear --colors --menu "Build Dreambox Image" 22 70 10 ${list[@]})
    case $box in
    1)
    machinespecific="dm900-original"
    boxsim="original"
    ;;
    2)
    machinespecific="dm900-clone"
    boxsim="clone"
    ;;
    3)
    machinespecific="dm920-original"
    boxsim="original"
    ;;
    *) clear && exit ;;
    esac

clear
## Menu Select build type ##
TYPE_1="openatv"
TYPE_2="openbh"
list=
for i in $(seq 1 2); do
    p="TYPE_$i"
    list="$list $i ${!p} "
done
list=($list)
build=$(dialog --stdout --clear --colors --menu "Select build type" 12 60 10 ${list[@]})
    case $build in
    1)
    echostr="Compiling openatv $machinespecific image, please wait ..."
    MAKETYPE="openatv"
    ;;
    2)
    echostr="Compiling openbh $machinespecific image, please wait ..."
    MAKETYPE="openbh"
    ;;
    *) clear && exit ;;
    esac

clear

if [ "$machinespecific" = "dm900-clone" ]; then
    cp -f backup/dm900-clone/* meta-dream/recipes-local/drivers/
    echo "$echostr"
    MACHINE=dm900 DISTRO=${MAKETYPE} DISTRO_TYPE=release MACHINESIM=${boxsim} make image
elif [ "$machinespecific" = "dm900-original" ]; then
    cp -f backup/dm900-original/* meta-dream/recipes-local/drivers/
    echo "$echostr"
    MACHINE=dm900 DISTRO=${MAKETYPE} DISTRO_TYPE=release MACHINESIM=${boxsim} make image
elif [ "$machinespecific" = "dm920" ]; then
    echo "$echostr"
    MACHINE=dm920 DISTRO=${MAKETYPE} DISTRO_TYPE=release MACHINESIM=${boxsim} make image
else
    echo "Please enter a correct choice"
fi
