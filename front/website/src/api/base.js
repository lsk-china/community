import axios from 'axios'

let baseAxios = axios.create({
  withCredentials: true,
  baseURL: 'http://localhost:9200/api/'
})

const base = {
  get: async function (url, data) {
    let resp = await baseAxios.request({
      url: url,
      method: 'GET',
      params: data
    })
    return resp
  },
  post: async function (url, data) {
    let resp = await baseAxios.request({
      url: url,
      method: 'POST',
      params: data
    })
    return resp
  }
}

export default base
