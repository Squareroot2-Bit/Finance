import { describe, it, expect, afterEach } from 'vitest'
import $http from '@/http/http_index'

import MockAdapter from 'axios-mock-adapter'
import { login, logout, register } from '@/http/api'

const mock = new MockAdapter($http)

describe('API Functions', () => {
  afterEach(() => {
    mock.reset()
  })

  it('登录成功', async () => {
    const loginData = { username: 'testuser', password: 'testpass' }
    mock
      .onPost('http://localhost:8080/login')
      .reply(200, { code: 0, message: '登录成功', data: null })

    const response = await login(loginData)
    expect(response.data).toEqual(null)
  })
  it('用户不存在', async () => {
    const loginData = { username: 'nonexistuser', password: 'testpass' }
    mock
      .onPost('http://localhost:8080/login')
      .reply(200, { code: -1, message: '用户不存在', data: null })

    try {
      await login(loginData)
    } catch (error) {
      expect(error).toEqual('用户不存在')
    }
  })
  it('密码错误', async () => {
    const loginData = { username: 'testuser', password: 'wrongpass' }
    mock
      .onPost('http://localhost:8080/login')
      .reply(200, { code: -2, message: '密码错误', data: null })

    try {
      await login(loginData)
    } catch (error) {
      expect(error).toEqual('密码错误')
    }
  })
  it('注册成功', async () => {
    const registerData = { username: 'testuser', password: 'testpass' }
    mock
      .onPost('http://localhost:8080/register')
      .reply(200, { code: 0, message: '注册成功', data: null })

    const response = await register(registerData)
    expect(response.data).toEqual(null)
  })
  it('用户名已存在', async () => {
    const loginData = { username: 'testuser', password: 'wrongpass' }
    mock
      .onPost('http://localhost:8080/login')
      .reply(200, { code: -3, message: '用户名已存在', data: null })

    try {
      await login(loginData)
    } catch (error) {
      expect(error).toEqual('用户名已存在')
    }
  })

  it('登出成功', async () => {
    mock.onGet('/logout').reply(200, { code: 0, message: '登出成功', data: null })
    const response = await logout()
    expect(response.data).toEqual(null)
  })
  it('登出失败', async () => {
    mock.onGet('/logout').reply(200, { code: -4, message: '登出失败', data: null })
    try {
      await logout()
    } catch (error) {
      expect(error).toEqual('登出失败')
    }
  })
})
