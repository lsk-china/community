// import axios from 'axios'
// import { createCookieHeader } from '../util/cookie'
//
// let authAxios = axios.create({
//   withCredentials: true,
//   baseURL: 'http://localhost:9200/api/auth/'
// })
//
// const auth = {
//   login: async function (identity, password, token, reqKey) {
//     let resp = null
//     try {
//       resp = await authAxios.post('/login', {
//         identity: identity,
//         password: password
//       }, {
//         headers: [{'Cookie': createCookieHeader({
//           token: token,
//           reqKey: reqKey
//         })}]
//       })
//       return resp.data
//     } catch (e) {
//       console.error(e)
//     }
//   },
//   token: function () {
//     authAxios.get('/token')
//   }
// }
//
// export default auth

import base from './base'

function transformURL (urlIn) {
  return '/auth' + urlIn
}

const auth = {
  token: function () {
      base.get(transformURL('/token'))
  },
  login: async function (identity, password) {
    let resp = null
    try {
      resp = await base.post(transformURL('/login'), {
        identity: identity,
        password: password
      })
      return resp.data
    } catch (e) {
      console.log(e)
    }
  }
}

export default auth
