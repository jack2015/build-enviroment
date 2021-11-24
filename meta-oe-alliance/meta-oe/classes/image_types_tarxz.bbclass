inherit image_types

IMAGEDIR = "${MACHINE}"
IMAGEVERSION = "${IMAGE_NAME}"

IMAGE_CMD_tar = "tar --sort=name --numeric-owner -cf ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tar -C ${IMAGE_ROOTFS} . || [ $? -eq 1 ]"

IMAGE_CMD_tar_prepend = " \
    mkdir -p ${IMAGE_ROOTFS}/tmp; \
    "

CONVERSION_CMD_xz = " \
    rm -f ${DEPLOY_DIR_IMAGE}/*.zip; \
    xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_DEFAULTS} --check=${XZ_INTEGRITY_CHECK} ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tar > ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.tar.xz; \
    echo "${IMAGEVERSION}" > ./imageversion; \
    zip ${IMAGEVERSION}.zip ./imageversion ./*.xz; \
    rm -f ./*.manifest; \
    rm -f ./imageversion; \
    "
