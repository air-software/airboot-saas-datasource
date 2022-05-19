// 全局混合的Vue实例
import { mapGetters, mapActions } from 'vuex'

export default {
  data() {
    return {
    }
  },
  computed: {
    // 引入所有vuex的getter，可以直接通过this来调用，属性名前加上$表示全局属性
    ...mapGetters({
      $loginTenant: 'tenant',
      $loginUser: 'user',
      $isTenantAdmin: 'isTenantAdmin'
    })
  },
  methods: {
    // 展开所有vuex的actions，可以直接通过this来调用，方法名前加上$表示全局方法
    ...mapActions({
    }),
    // 深拷贝
    $clone: obj => JSON.parse(JSON.stringify(obj)),
    /**
     * 将传入的参数对象附加到指定URL上
     * @param url 指定URL
     * @param params 参数对象
     */
    $paramsToUrl(url, params) {
      if (!params) return url
      let suffix = ''
      for (const key in params) {
        suffix += `&${key}=${params[key]}`
      }
      suffix = url.indexOf('?') > -1 ? suffix : suffix.replace('&', '?')
      return url + suffix
    },
    /**
     * 数组转对象
     * @param array
     * @param key 指定作为对象key的字段，最好是具有唯一值的字段
     */
    $arrayToObject(array, key) {
      if (!key) {
        throw 'Error: Need key'
      }

      let resultObj = {}
      array.forEach(item => {
        resultObj[item[key]] = item
      })
      return resultObj
    },
    /**
     * 根据key来合并相同值的项成数组，并最终转为对象
     * @param array
     * @param mergeKey 要根据哪个key来合并
     */
    $mergeArrayToObject(array, mergeKey) {
      if (!mergeKey) {
        throw 'Error: Need mergeKey'
      }

      let resultObj = {}
      array.forEach(item => {
        if (!resultObj[item[mergeKey]]) {
          // 初始化数组
          resultObj[item[mergeKey]] = []
          resultObj[item[mergeKey]].push(item)
        } else if (resultObj[item[mergeKey]][0][mergeKey] === item[mergeKey]) {
          // 如果mergeKey对应的值相同，则合并至同一数组
          resultObj[item[mergeKey]].push(item)
        }
      })
      return resultObj
    },
    /**
     * 解析extJson
     * @param form 表单对象
     * @param keys 如果是字符串则直接作为key解析，如果是数组则遍历解析
     */
    $parseFormExtJson(form, keys) {
      if (form.extJson) {
        const ext = JSON.parse(form.extJson)
        if (typeof keys === 'string') {
          form[keys] = ext[keys]
        } else {
          keys.forEach(key => {
            form[key] = ext[key]
          })
        }
      }
    },
    /**
     * 序列化extJson
     * @param form 表单对象
     * @param keys 如果是字符串则直接作为key解析，如果是数组则遍历解析
     */
    $seriFormExtJson(form, keys) {
      let ext = form.ext || {}
      if (typeof keys === 'string') {
        ext[keys] = form[keys]
      } else {
        keys.forEach(key => {
          ext[key] = form[key]
        })
      }
      form.extJson = JSON.stringify(ext)
    }
  }
}
