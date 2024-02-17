#!/bin/bash

# 스크립트 파라미터 할당
$BUILD_RESULT=$1
APP_NAME=$2
OCI_BUILD_RUN_ID=$3
OCI_BUILD_STAGE_NAME=$4
DISCORD_WEBHOOK_URL=$5
IMAGE_TAG=${6:-}  # 6번째 파라미터가 제공되지 않으면 빈 문자열로 설정

# 빌드 결과에 따른 메시지 설정
if [ "$BUILD_RESULT" == "SUCCESS" ]; then
    MESSAGE="✅ Build succeeded!"
else
    MESSAGE="❌ Build failed!"
fi

# IMAGE_TAG가 제공된 경우 메시지에 추가
if [ -n "$IMAGE_TAG" ]; then
    IMAGE_TAG_MESSAGE="\\nIMAGE_TAG: ${IMAGE_TAG}"
else
    IMAGE_TAG_MESSAGE=""
fi

# Discord 웹훅을 통해 메시지 전송
curl -H "Content-Type:application/json" \
     -d "{\"content\": \"$MESSAGE\\nApp: ${APP_NAME}_${OCI_BUILD_RUN_ID}\\nName: ${OCI_BUILD_STAGE_NAME}${IMAGE_TAG_MESSAGE}\n\" }" \
     $DISCORD_WEBHOOK_URL
