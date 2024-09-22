<template>
  <div>
    <button @click="submitFile">上传</button>
    <div class="about">
      <LayUpload v-model="file" :auto="false" :cut="true" :drag="true" multiple @done="doneHandle">
        <template #preview>
          <img v-if="data" :src="data.url" alt="上传图片预览" style="width: 100px"/>
        </template>
      </LayUpload>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, ref} from "vue";
import COS from "cos-js-sdk-v5";
import {createTempSecret} from "@/apis/cos";

const data = ref();
const username = ref("username");
const filename = ref("/test.jpg");
const allowExt = ["jpg", "png"];

onMounted(() => {
  console.log("onMounted");
});

const file = ref([]);
const doneHandle = (result: { data: string }) => {
  data.value = JSON.parse(result.data);
  console.log("文件上传成功", data.value.url);
};

const submitFile = () => {
  console.log("开始上传文件", file.value[0]);

  const cos = new COS({
    getAuthorization: function (options, callback) {
      // 初始化时不会调用，只有调用 cos 方法（例如 cos.putObject）时才会进入
      // 异步获取临时密钥
      createTempSecret().then((response) => {
        console.log("获取临时密钥", response);
        callback({
          TmpSecretId: response.result.tmpSecretId, // 临时密钥的 tmpSecretId
          TmpSecretKey: response.result.tmpSecretKey, // 临时密钥的 tmpSecretKey
          SecurityToken: response.result.sessionToken, // 临时密钥的 sessionToken
          // 建议返回服务器时间作为签名的开始时间，避免用户浏览器本地时间偏差过大导致签名错误
          StartTime: response.result.startTime, // 时间戳，单位秒，如：1580000000
          ExpiredTime: response.result.expiredTime, // 临时密钥失效时间戳，是申请临时密钥时，时间戳加 durationSeconds
        });
      });
    },
  });
  cos.uploadFile({
    Bucket: "anime-1255705827" /* 填写自己的 bucket，必须字段 */,
    Region: "ap-guangzhou" /* 存储桶所在地域，必须字段 */,
    Key: "test/1.jpg" /* 存储在桶里的对象键（例如1.jpg，a/b/test.txt），必须字段 */,
    Body: file.value[0], // 上传文件对象
    SliceSize: 1024 * 1024 * 5 /* 触发分块上传的阈值，超过5MB 使用分块上传，小于5MB使用简单上传。可自行设置，非必须 */,
  });
};
</script>

<style scoped>
</style>
