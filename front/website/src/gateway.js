import axios from 'axios'

let gatewayAxios = axios.create({
  withCredentials: true,
  baseURL: 'http://localhost:11003/'
})

const gateway = {
  generateCaptcha: async function () {
    let resp = null
    try {
      resp = await gatewayAxios('/generateCaptcha')
      return resp.data
    } catch (e) {
      console.error(e)
    }
  },
  checkCaptcha: async function (codeID, codeText, targetURL) {
    let resp = null
    try {
      resp = await gatewayAxios.get('/checkCaptcha', {
        params: {
          codeID: codeID,
          codeText: codeText,
          targetURL: targetURL
        }
      })
      return resp
    } catch (e) {
      console.log(e)
    }
  }
}

export default gateway
