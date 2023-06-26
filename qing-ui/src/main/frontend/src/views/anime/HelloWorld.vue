<template>
  <div class="hello">
    <div>
      <b-button>Button</b-button>
      <b-button variant="danger">Button</b-button>
      <b-button variant="success">Button</b-button>
      <b-button variant="outline-primary">Button</b-button>
      <b-icon animation="spin-reverse" font-scale="1" icon="clock"></b-icon>
      <b-badge>这是你的名字</b-badge>
    </div>
    <iframe
      allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
      allowfullscreen
      height="360"
      src="https://www.youtube.com/embed/ylLOn6KXzAc"
      width="640"
    ></iframe>
    <div>
      <b-iconstack font-scale="2" variant="white">
        <b-icon icon="square-fill" stacked variant="dark"></b-icon>
        <b-icon
          icon="arrow-up-short"
          scale="0.5"
          shift-h="-3"
          shift-v="3"
          stacked
        ></b-icon>
        <b-icon
          icon="arrow-up-short"
          rotate="90"
          scale="0.5"
          shift-h="3"
          shift-v="3"
          stacked
        ></b-icon>
        <b-icon
          icon="arrow-up-short"
          rotate="180"
          scale="0.5"
          shift-h="3"
          shift-v="-3"
          stacked
        ></b-icon>
        <b-icon
          icon="arrow-up-short"
          rotate="270"
          scale="0.5"
          shift-h="-3"
          shift-v="-3"
          stacked
        ></b-icon>
      </b-iconstack>
      <b-iconstack font-scale="5">
        <b-icon icon="square" stacked></b-icon>
        <b-icon icon="dot" shift-h="-3" shift-v="4" stacked></b-icon>
        <b-icon icon="dot" shift-h="-3" stacked></b-icon>
        <b-icon icon="dot" shift-h="-3" shift-v="-4" stacked></b-icon>
        <b-icon icon="dot" shift-h="3" shift-v="4" stacked></b-icon>
        <b-icon icon="dot" shift-h="3" stacked></b-icon>
        <b-icon icon="dot" shift-h="3" shift-v="-4" stacked></b-icon>
      </b-iconstack>
    </div>
    <div>
      <b-button class="mb-2" size="sm">
        <b-icon aria-hidden="true" icon="gear-fill"></b-icon>
        Settings
      </b-button>
      <br />
      <b-button class="mb-2" variant="primary">
        Pay now
        <b-icon aria-hidden="true" icon="credit-card"></b-icon>
      </b-button>
      <br />
      <b-button class="mb-2" variant="outline-info">
        <b-icon aria-hidden="true" icon="power"></b-icon>
        Logout
      </b-button>
      <br />
      <b-button class="mb-2" size="lg" variant="info">
        <b-icon aria-label="Help" icon="question-circle-fill"></b-icon>
      </b-button>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  props: {
    title: String,
    likes: Number,
  },
  name: "HelloWorld",
  data() {
    return {
      msg: "Welcome to Your Vue.js App",
    };
  },
};
</script>

<style scoped></style>
