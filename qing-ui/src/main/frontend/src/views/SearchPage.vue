<template>
  <div id="container">
    <div class="title">
      关键词搜索结果如下，共<span style="color: red">{}</span>页
    </div>
    <hr/>
    <div class="content-container">
      <ul>
        <li>
          <div class="d-flex">
            <div class="video_cover">
              <div class="video_cover_wrapper">
                <a
                  href="http://www.agedm.org/detail/20090018"
                  class="d-block"
                  title="七鬼神"
                ><img
                  class="video_thumbs lazyload"
                  src="https://cdn.aqdstatic.com:966/age/20090018.jpg"
                  data-original="https://cdn.aqdstatic.com:966/age/20090018.jpg"
                  alt="七鬼神"
                  style="display: inline"
                /></a>
                <span class="video_play_status rounded text-truncate"
                >[TV 01-25]</span
                >
              </div>
            </div>
            <div class="card-body">
              <h5 class="card-title">
                <a href="http://www.agedm.org/detail/20090018">七鬼神</a>
              </h5>
              <div class="video_detail_info"><span>动画种类：</span>TV</div>
              <div class="video_detail_info">
                <span>原版名称：</span>07-GHOST
              </div>
              <div class="video_detail_info">
                <span>其他名称：</span>神幻拍档
              </div>
              <div class="video_detail_info">
                <span>首播时间：</span>2009-04-06
              </div>
              <div class="video_detail_info"><span>播放状态：</span>完结</div>
              <div class="video_detail_info">
                <span>原作：</span>雨宫由树/市原雪乃
              </div>
              <div class="video_detail_info">
                <span>剧情类型：</span>战斗 冒险 奇幻
              </div>
              <div class="video_detail_info">
                <span>制作公司：</span>スタジオディーン
              </div>
              <div class="video_detail_info desc">
                <span>简介：</span
                >《07-GHOST～神幻拍档～》是根据雨宫由树、市原雪乃创作的同名漫画改编的动画，由Studio
                DEEN制作。于2009年4月6日至2009年9月22日在日本播出。千年以前，世界上存在着两个有着相同实力的强大的国家。一个是由被称为“拉法艾尔之瞳”的神所加护的巴尔斯布鲁克帝国，以及由名为“弥卡艾尔之眼”的神所加护的拉古斯王国。两国之间为了维护世界和平而订立协定，长期以来互相合作。不过就在十
                年前，传言恶之死神“费亚罗廉”的身体将继续沉眠。围绕着装有费亚罗廉沉睡千年肉体的“潘多拉之盒”引发了激战，导致了拉古斯王国的毁灭。据巴尔斯布鲁克帝国的史书所记载的历史称，是因为拉古斯王国打算取得两块圣石——“拉法艾尔之眼”和“弥卡艾尔之眼”，而破坏了双方的协定。最后和巴尔斯布鲁克帝国对立而走向毁灭。10年后，在拉古斯王国的城池陷落前，曾有一个戴着誓言颈环的男子出现在泰德的梦中，并告诉他：“泰德，总有一天你会知道自己真正的使命是什么的。”而这个梦反复出现在他的梦中，使泰德困惑不已。此时的他和好友米卡杰已经成为了巴尔斯布鲁克帝国陆军士官学校的优秀学生。他们为了能进入梦想已久的霍普鲁克要塞而向毕业考试发起了挑战。对于毕业后的生活充满了期待。可是，当泰德遇到了率领着战斗部队的阿亚纳米参谋长时，他的记忆恢复了。“梦中见到的男人。”在旁边站着的是拿着染满鲜血的宝剑的阿亚纳米.而那个静静地躺卧在血海中的男人，“那是……我的父亲！”泰德终于想起来自己是拉古斯的亡国王子。于是，泰德逃离了军队。
              </div>
              <div class="video_btns">
                <a
                  href="http://www.agedm.org/detail/20090018"
                  class="btn btn-sm btn-secondary me-2"
                >资源详情</a
                >
                <a
                  href="http://www.agedm.org/play/20090018/1/1"
                  class="btn btn-sm btn-danger"
                >在线播放</a
                >
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: "HomePage",
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
      currentDate: new Date(),
      time: "2020年01月15日 21时50分19秒",
      loading: true,
      videoUrl:
        "https://1251316161.vod2.myqcloud.com/007a649dvodcq1251316161/75b32a815285890809903778875/5cAXY7aMUEQA.mp4",
      animeInfoList: [
        {
          id: 20000001,
          name: "海贼王",
          instruction:
            "电视动画《航海王》改编自尾田荣一郎创作的同名长篇少年漫画，动画由东映动画制作。 海贼王哥尔·D·罗杰在临死前曾留下了关于其毕生的财富“One Piece”的消息，由此引得群雄并起，众海盗们为了这笔传说中的巨额财富展开争夺，各种势力、政权不断交替，整个世界进入了动荡混乱的“大海贼时代”。 生长在东海某小村庄的路飞受到海贼香克斯的精神指引，决定成为一名出色的海盗。为了达成这个目标，并找到万众瞩目的One Piece，路飞踏上艰苦的旅程。一路上他遇到了无数磨难，也结识了索隆、娜美、山治、乌索普、罗宾等一众性格各异的好友。他们携手一同展开充满传奇色彩的大冒险。",
          districtId: 1,
          districtName: "日本",
          coverUrl:
            "http://localhost/age/H6eec4b52f5ed449b8583e5ab518e7849p.jpg",
          typeId: 1,
          typeName: "TV",
          originalName: "ONE PIECE",
          otherName: "航海王 / ワンピース",
          author: "尾田栄一郎",
          company: "東映動画",
          premiereDate: "2022-01-11",
          playStatus: "连载",
          plotType: "搞笑 冒险 热血 励志",
          tags: "搞笑 冒险 热血 励志",
          officialWebsite: "https://www.fujitv.co.jp/b_hp/onepiece/index.html",
          playHeat: "3953",
        },
      ],
    };
  },
  mounted() {},
  methods: {
    handleSelect(key: string, keyPath: string) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style>
#container {
  width: 100%;
  height: 200px;
}
</style>
