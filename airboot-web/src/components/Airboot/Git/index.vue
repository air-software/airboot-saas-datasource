<template>
  <div>
    <svg-icon :icon-class="gitMap[platform].icon" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'AirbootGit',
  props: {
    platform: {
      type: String,
      default: 'github'
    }
  },
  data() {
    return {
      gitMap: {
        github: {
          url: 'https://github.com/air-software/airboot-saas-datasource',
          icon: 'github'
        },
        gitee: {
          url: 'https://gitee.com/air-soft/airboot-saas-datasource',
          icon: 'gitee'
        }
      }
    }
  },
  methods: {
    goto() {
      window.open(this.gitMap[this.platform].url)
    }
  }
}
</script>
