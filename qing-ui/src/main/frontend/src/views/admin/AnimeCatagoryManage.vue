<template>
  <div ref="fullscreenTargetRef2" style="height: 800px; background-color: #000000">
    <lay-table v-if="!isEdit" ref="tableRef" v-model:selectedKey="selectedKey" v-model:selectedKeys="selectedKeys" :autoColsWidth="true" :columns="columns" :data-source="dataSource" :default-toolbar="toolbar" :ellipsisTooltip="true" :even="true" :loading="loading" :page="page" :resize="false" size="md">
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
  </div>
</template>
<style lang="scss" scoped></style>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {layer} from "@layui/layui-vue";
import {createAnime, findById} from "@/apis/anime";
import {getCategoryList} from "@/apis/categories";
import type {Anime} from "@/apis/anime/types";
import type {Category} from "@/apis/categories/types";

onMounted(() => {
  resolveCategoryList();
});

const fullscreenTargetRef2 = ref(null);

const loading = ref(false);
const tableRef = ref();
const toolbar = ["filter", "export", "print"];
const selectedKey = ref("1");
const selectedKeys = ref(["2", "3", "6", "7"]);

const page = reactive({current: 1, limit: 10, total: 100});

const recommend = () => {
  layer.msg("添加推荐" + selectedKeys.value);
};

const columns = [
  {title: "名称", width: "80px", key: "name"},
  {title: "备注", width: "80px", key: "author"},
  {title: "创建时间", width: "80px", key: "createdAt"},
  {title: "最后更新时间", width: "80px", key: "updateAt"},
  {title: "操作", width: "150px", customSlot: "operator", key: "operator", fixed: "right", ignoreExport: true},
];

const animeType = ref([1]);
// 编辑动漫信息相关
const animeDetailForm = ref<Anime>({} as Anime);
const editShade = ref(false);
const isEdit = ref(false);

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
        layer.confirm("确认操作", {shade: false});
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

const dataSource = ref<Category[]>();

const view = (anime: any) => {
  isEdit.value = true;
  findById(anime.id).then((res) => {
    console.log(res);
    editShade.value = true;
    animeDetailForm.value = res.result;
  });
};

function reload() {
}

function resolveCategoryList() {
  loading.value = true;
  getCategoryList({
    pageSize: 10,
    page: page.current - 1,
  }).then((response) => {
    dataSource.value = response.result.list;
    console.log("加载动漫信息成功", response);
    page.current = response.result.pageNumber + 1;
    loading.value = false;
  });
}
</script>
