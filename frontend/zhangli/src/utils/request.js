import axios from 'axios'

// 统一配置
let GLOABLE_REQUEST = axios.create({
  baseURL: 'http://localhost:8080/',
  responseType: 'json',
  validateStatus (status) {
    // 200 外的状态码都认定为失败
    return status === 200
  }
})


const request = {

  get (url, params) {
    let _params
    console.log(params)
    if (Object.is(params, undefined)) {
      _params = ''
    } else {
      _params = '?'
      for (let key in params) {
        if (params.hasOwnProperty(key) && params[key] !== null) {
          _params += `${key}=${params[key]}&`
        }
      }
    }
    return GLOABLE_REQUEST.get(`${url}${_params}`)
  },
  post(url,params){

    let _params
    console.log(params)
    if (Object.is(params,undefined)){
      _params=''
    } else {
      for (let key in params) {
        if (params.hasOwnProperty(key)&&params[key]!=null){
        }
      }
    }

    return GLOABLE_REQUEST.post(url,params)
  }
}
export default request
