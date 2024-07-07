<template>
  <div ref="fullscreenTargetRef2" style="height: 800px; background-color: #000000">
    <lay-table v-if="!isEdit" ref="tableRef" v-model:selectedKey="selectedKey" v-model:selectedKeys="selectedKeys" :autoColsWidth="true" :columns="columns" :data-source="dataSource" :default-toolbar="toolbar" :ellipsisTooltip="true" :even="true" :loading="loading" :page="page" :resize="false" size="md" @change="change">
      <template #toolbar>
        <lay-space>
          <lay-button size="md" type="primary" @click="recommend">添加推荐</lay-button>
          <lay-button size="md" type="primary" @click="addAnime">新增</lay-button>
          <lay-button size="md" type="normal" @click="reload">刷新</lay-button>
        </lay-space>
      </template>
      <template #operator="{ row }">
        <lay-button size="xs" type="primary" @click="view(row)">编辑</lay-button>
        <lay-button size="xs" @click="view(row)">详情</lay-button>
      </template>
    </lay-table>

    <lay-layer v-model="isEdit" :area="['1000px', '800px']" :btn="editorAction" shade="false">
      <div style="color: rebeccapurple; margin-right: 1rem">
        <lay-form v-if="editShade" ref="editorFormRef" :model="animeDetailForm" required>
          <lay-form-item label="账户" prop="username">
            <lay-input id="username" v-model="animeDetailForm.name"></lay-input>
          </lay-form-item>
          <lay-form-item label="作者" prop="author">
            <lay-input id="author" v-model="animeDetailForm.author"></lay-input>
          </lay-form-item>
          <lay-form-item label="制作公司" prop="companyName">
            <lay-select v-model="animeDetailForm.companyId" :show-search="true" style="width: 100%">
              <lay-select-option :value="1" label="原力动画"></lay-select-option>
            </lay-select>
          </lay-form-item>
          <lay-form-item label="剧情类型" prop="plotType">
            <lay-input id="plotType" v-model="animeDetailForm.plotType">></lay-input>
          </lay-form-item>
          <!--          <lay-form-item label="分类" prop="typeId">-->
          <!--            <lay-select style="width: 100%">-->
          <!--              <lay-select-option :value="1" label="中国"></lay-select-option>-->
          <!--              <lay-select-option :value="2" label="美国"></lay-select-option>-->
          <!--              <lay-select-option :value="3" label="法国"></lay-select-option>-->
          <!--            </lay-select>-->
          <!--          </lay-form-item>-->
          <lay-form-item :required="false" label="首播日期" prop="premiereDate">
            <lay-date-picker v-model="animeDetailForm.premiereDate" allowClear placeholder="选择首播日期"></lay-date-picker>
          </lay-form-item>
          <lay-form-item label="地区" prop="districtId">
            <lay-select v-model="animeDetailForm.districtId" style="width: 100%">
              <lay-select-option v-for="{ id, name } of districtInfoList" :label="name" :value="id"></lay-select-option>
            </lay-select>
          </lay-form-item>
          <lay-form-item label="类型">
            <lay-checkbox-group v-model="animeType">
              <lay-checkbox name="like" skin="primary" value="1">写作</lay-checkbox>
              <lay-checkbox name="like" skin="primary" value="2">画画</lay-checkbox>
              <lay-checkbox name="like" skin="primary" value="3">运动</lay-checkbox>
            </lay-checkbox-group>
          </lay-form-item>
          <lay-form-item label="介绍" prop="desc">
            <lay-textarea v-model="animeDetailForm.instruction" placeholder="请输入描述"></lay-textarea>
          </lay-form-item>
        </lay-form>
      </div>
    </lay-layer>

    <lay-fullscreen v-slot="{ enter, exit, toggle, isFullscreen }" :immersive="true" :target="fullscreenTargetRef2" position="absolute" zIndex="12000">
      <lay-layer v-model="addForm.isAdd" :area="['1000px', '800px']" :btn="addForm.addAction" shade="false" title="新增动漫">
        <lay-scroll height="600px">
          <div style="color: rebeccapurple; margin-right: 1rem">
            <lay-form ref="addFormRef" :model="addForm.formData" required>
              <lay-form-item label="名称" prop="name">
                <lay-input id="name" v-model="addForm.formData.name"></lay-input>
              </lay-form-item>
              <lay-form-item label="原作" prop="originalName">
                <lay-input id="originalName" v-model="addForm.formData.originalName"></lay-input>
              </lay-form-item>
              <lay-form-item label="作者" prop="author">
                <lay-input id="author" v-model="addForm.formData.author"></lay-input>
              </lay-form-item>
              <lay-form-item label="公司" prop="companyName">
                <lay-select v-model="addForm.formData.companyId" :show-search="true" style="width: 100%">
                  <lay-select-option :value="1" label="原力动画"></lay-select-option>
                </lay-select>
              </lay-form-item>
              <lay-form-item label="分类" prop="typeId">
                <lay-select v-model="addForm.formData.typeId" style="width: 100%">
                  <lay-select-option v-for="{ id, name } of categoryList" :label="name" :value="id"></lay-select-option>
                </lay-select>
              </lay-form-item>
              <lay-form-item label="剧情类型" prop="plotType">
                <lay-input id="plotType" v-model="addForm.formData.plotType">></lay-input>
              </lay-form-item>
              <lay-form-item :required="false" label="标签">
                <lay-select v-model="addForm.formData.tagIds" :multiple="true" :show-search="true">
                  <lay-select-option v-for="{ id, name } of tagInfoList" :label="name" :value="id"></lay-select-option>
                </lay-select>
              </lay-form-item>
              <lay-form-item :required="false" label="首播日期" prop="premiereDate">
                <lay-date-picker v-model="addForm.formData.premiereDate" allowClear placeholder="选择首播日期"></lay-date-picker>
              </lay-form-item>
              <lay-form-item label="地区" prop="districtId">
                <lay-select v-model="addForm.formData.districtId" style="width: 100%">
                  <lay-select-option v-for="{ id, name } of districtInfoList" :label="name" :value="id"></lay-select-option>
                </lay-select>
              </lay-form-item>
              <lay-form-item label="官方网站" prop="desc">
                <lay-input v-model="addForm.formData.officialWebsite" :disabled="false"></lay-input>
              </lay-form-item>
              <lay-form-item label="类型">
                <lay-checkbox-group v-model="addForm.formData.typeId">
                  <lay-checkbox name="like" skin="primary" value="1">写作</lay-checkbox>
                  <lay-checkbox name="like" skin="primary" value="2">画画</lay-checkbox>
                  <lay-checkbox name="like" skin="primary" value="3">运动</lay-checkbox>
                </lay-checkbox-group>
              </lay-form-item>
              <lay-form-item label="介绍" prop="desc">
                <lay-textarea v-model="addForm.formData.instruction" placeholder="请输入描述"></lay-textarea>
              </lay-form-item>
            </lay-form>
          </div>
        </lay-scroll>
      </lay-layer>
    </lay-fullscreen>
  </div>
</template>
<style lang="scss" scoped></style>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {layer} from "@layui/layui-vue";
import {createAnime, findById, getAnimeList, updateAnime} from "@/apis/anime";
import type {Anime} from "@/apis/anime/types";
import {getTagList} from "@/apis/tags";
import type {Tag} from "@/apis/tags/types";
import {getDistrictList} from "@/apis/district";
import type {District} from "@/apis/district/types";
import {getCategoryList} from "@/apis/categories";
import type {Category} from "@/apis/categories/types";

onMounted(() => {
  resolveCategories();
  resolveTags();
  resolveDistricts();
  reloadAnimeInfo();
});

const fullscreenTargetRef2 = ref(null);

const loading = ref(false);
const tableRef = ref();
const toolbar = ["filter", "export", "print"];
const selectedKey = ref("1");
const selectedKeys = ref(["2", "3", "6", "7"]);
const tags = ref([{}]);

const page = reactive({ current: 1, limit: 10, total: 100 });

const recommend = () => {
  layer.msg("添加推荐" + selectedKeys.value);
};

const getCheckData3 = () => {};

const columns = [
  { fixed: "left", type: "checkbox", title: "复选" },
  { title: "名称", width: "80px", key: "name" },
  { title: "作者", width: "80px", key: "author" },
  { title: "公司名称", key: "companyName", width: "80px" },
  { title: "创建时间", width: "80px", key: "createdAt" },
  { title: "热度", width: "80px", key: "playHeat" },
  { title: "播放状态", width: "80px", key: "playStatus" },
  { title: "动漫类型", width: "120px", key: "plotType" },
  { title: "首播时间", width: "120px", key: "premiereDate" },
  {
    title: "备注",
    width: "400px",
    key: "instruction",
    ellipsisTooltip: true,
  },
  { title: "操作", width: "150px", customSlot: "operator", key: "operator", fixed: "right", ignoreExport: true },
];

const animeType = ref([1]);
// 编辑动漫信息相关
const animeDetailForm = ref<Anime>({} as Anime);
const editShade = ref(false);
const isEdit = ref(false);
const editorFormRef = ref();
const editorAction = ref([
  {
    text: "确认",
    callback: () => {
      console.debug(animeDetailForm.value);
      updateAnime(editAnimeId.value, animeDetailForm.value);
      layer.confirm("确认操作", { shade: false });
    },
  },
  {
    text: "关闭",
    callback: () => {
      console.debug("关闭更新弹窗");
      isEdit.value = false;
      reloadAnimeInfo();
    },
  },
]);

// 新增动漫
const addFormRef = ref();
const addForm = reactive({
  formData: {} as Anime,
  isAdd: false,
  addFormRef: addFormRef,
  addAction: [
    {
      text: "确认",
      callback: () => {
        console.debug("新增动漫信息", addForm.formData);
        createAnime(addForm.formData);
        layer.confirm("确认操作", { shade: false });
      },
    },
    {
      text: "关闭",
      callback: () => {
        console.debug("关闭更新弹窗");
        addForm.isAdd = false;
      },
    },
  ],
});

const addAnime = () => {
  addForm.isAdd = true;
};

const reload = () => {
  reloadAnimeInfo();
};

const change = (newPage: any) => {
  console.debug("分页信息发生变化", newPage);
  page.current = newPage.current;
  reloadAnimeInfo();
};

const tagInfoList = ref<Tag[]>();
const categoryList = ref<Category[]>();
const districtInfoList = ref<District[]>();
const dataSource = ref<Anime[]>();

const editAnimeId = ref();

const view = (anime: any) => {
  isEdit.value = true;
  findById(anime.id).then((res) => {
    console.log(res);
    editShade.value = true;
    animeDetailForm.value = res.result;
  });
};

function resolveTags() {
  getTagList({
    pageSize: "100",
    page: 0,
  }).then((tagResponse) => {
    console.log("获取标签信息成功", tagResponse);
    tagInfoList.value = tagResponse.result.list;
  });
}

function resolveCategories() {
  getCategoryList({
    pageSize: "100",
    page: 0,
  }).then((tagResponse) => {
    console.debug("获取获取类别信息响应", categoryList.value);
    categoryList.value = tagResponse.result.list;
    console.debug("获取获取类别信息成功", categoryList.value);
  });
}

function resolveDistricts() {
  getDistrictList({
    pageSize: "100",
    page: 0,
  }).then((tagResponse) => {
    console.log("获取区域信息成功", tagResponse);
    districtInfoList.value = tagResponse.result.list;
  });
}

function reloadAnimeInfo() {
  loading.value = true;
  getAnimeList({
    pageSize: "10",
    page: page.current - 1,
  }).then((anime) => {
    loading.value = false;
    dataSource.value = anime.result.list;
    console.log("加载动漫信息成功", anime);
    page.current = anime.result.pageNumber + 1;
    page.total = anime.result.total;
  });
}
</script>
