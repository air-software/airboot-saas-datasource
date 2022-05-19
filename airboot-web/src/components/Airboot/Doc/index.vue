<template>
  <div>
    <svg-icon icon-class="question" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'AirbootDoc',
  data() {
    return {
      url: 'https://github.com/air-software/airboot-saas-datasource'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
