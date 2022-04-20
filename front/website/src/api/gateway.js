// import axios from 'axios'
//
// let gatewayAxios = axios.create({
//   withCredentials: true,
//   baseURL: 'http://localhost:9200/api/gateway/'
// })
//
// const gateway = {
//   generateCaptcha: async function () {
//     let resp = null
//     try {
//       resp = await gatewayAxios('/generateCaptcha')
//       return resp.data
//     } catch (e) {
//       console.error(e)
//     }
//   },
//   checkCaptcha: async function (codeID, codeText, targetURL) {
//     let resp = null
//     try {
//       resp = await gatewayAxios.get('/checkCaptcha', {
//         params: {
//           codeID: codeID,
//           codeText: codeText,
//           targetURL: targetURL
//         }
//       })
//       return resp.data
//     } catch (e) {
//       console.log(e)
//     }
//   }
// }
//
// export default gateway

import base from './base'

function transformURL (urlIn) {
  return '/gateway' + urlIn
}

const gateway = {
  generateCaptcha: async function () {
    let resp = null
    try {
      resp = await base.get(transformURL('/generateCaptcha'), {})
      return new Promise(solved => solved(resp.data))
    } catch (e) {
      console.log(e)
    }
  },
  checkCaptcha: async function (codeID, codeText, targetURL) {
    let resp = null
    try {
      resp = await base.get(transformURL('/checkCaptcha'), {
        codeID: codeID,
        codeText: codeText,
        targetURL: targetURL
      })
      return resp.data
    } catch (e) {
      console.log(e)
    }
  }
}

export default gateway
