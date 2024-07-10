import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import LoginView from '@/views/LoginView.vue'
import { config } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { warn } from 'console'
config.global.plugins = [ElementPlus]
describe('LoginView.vue', () => {
  let wrapper: any

  beforeEach(() => {
    wrapper = mount(LoginView)
  })

  it('显示用户名输入框', async () => {
    const usernameInput = wrapper.findComponent({ name: 'ElInput' })
    await usernameInput.setValue('testuser')
    expect(wrapper.vm.loginForm.userName).toBe('testuser')
  })

  it('显示密码输入框', async () => {
    const passwordInput = wrapper.findAllComponents({ name: 'ElInput' })[1]
    await passwordInput.setValue('testpassword')
    expect(wrapper.vm.loginForm.password).toBe('testpassword')
  })

  it('显示登录按钮', () => {
    const loginButton = wrapper.find('button[name="login"]')
    expect(loginButton.exists()).toBe(true)
  })

  it('检测无效用户名', async () => {
    const formEl = {
      validate: vi.fn().mockImplementation((callback) => {
        callback(false, { field: ['error message'] })
      })
    }

    const consoleMock = {
      log: vi.fn(),
      warn: vi.fn()
    }

    Object.defineProperty(global, 'console', {
      value: consoleMock
    })

    const usernameInput = wrapper.findComponent({ name: 'ElInput' })
    const passwordInput = wrapper.findAllComponents({ name: 'ElInput' })[1]
    await usernameInput.setValue('')
    await passwordInput.setValue('aaa')
    await wrapper.find('button[name="login"]').trigger('click')
    await wrapper.vm.submitForm(formEl)

    expect(formEl.validate).toHaveBeenCalled()
    expect(consoleMock.log).toHaveBeenCalledWith('error submit!', { field: ['error message'] })
  })
  it('检测无效密码', async () => {
    const formEl = {
      validate: vi.fn().mockImplementation((callback) => {
        callback(false, { field: ['error message'] })
      })
    }

    const consoleMock = {
      log: vi.fn(),
      warn: vi.fn()
    }

    Object.defineProperty(global, 'console', {
      value: consoleMock
    })

    const usernameInput = wrapper.findComponent({ name: 'ElInput' })
    const passwordInput = wrapper.findAllComponents({ name: 'ElInput' })[1]
    await usernameInput.setValue('validuser')
    await passwordInput.setValue('')
    await wrapper.find('button[name="login"]').trigger('click')
    await wrapper.vm.submitForm(formEl)

    expect(formEl.validate).toHaveBeenCalled()
    expect(consoleMock.log).toHaveBeenCalledWith('error submit!', { field: ['error message'] })
  })
  it('密码有效', async () => {
    const formEl = {
      validate: vi.fn().mockImplementation((callback) => {
        callback(false, { field: ['error message'] })
      })
    }

    const consoleMock = {
      log: vi.fn(),
      warn: vi.fn()
    }

    Object.defineProperty(global, 'console', {
      value: consoleMock
    })

    const usernameInput = wrapper.findComponent({ name: 'ElInput' })
    const passwordInput = wrapper.findAllComponents({ name: 'ElInput' })[1]
    await usernameInput.setValue('validuser')
    await passwordInput.setValue('validpassword')
    await wrapper.find('button[name="login"]').trigger('click')
    await wrapper.vm.submitForm(formEl)

    expect(formEl.validate).toHaveBeenCalled()
    expect(consoleMock.log).toHaveBeenCalledWith('error submit!', { field: ['error message'] })
  })
})
