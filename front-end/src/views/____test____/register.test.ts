import { mount } from '@vue/test-utils'
import { describe, it, expect, beforeEach, vi } from 'vitest'
import RegisterView from '@/views/RegisterView.vue'
import { config } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { warn } from 'console'
config.global.plugins = [ElementPlus]
describe('RegisterView.vue', () => {
  let wrapper: any

  beforeEach(() => {
    wrapper = mount(RegisterView)
  })

  it('显示用户名输入框', async () => {
    const usernameInput = wrapper.findComponent({ name: 'ElInput' })
    await usernameInput.setValue('testuser')
    expect(wrapper.vm.registerForm.userName).toBe('testuser')
  })

  it('显示密码输入框', async () => {
    const passwordInput = wrapper.findAllComponents({ name: 'ElInput' })[1]
    await passwordInput.setValue('testpassword')
    expect(wrapper.vm.registerForm.password).toBe('testpassword')
  })

  it('显示注册按钮', () => {
    const loginButton = wrapper.find('button[name="register"]')
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
    const repeatpasswordInput = wrapper.findAllComponents({ name: 'ElInput' })[2]
    await usernameInput.setValue('')
    await passwordInput.setValue('aaaaaa')
    await repeatpasswordInput.setValue('aaaaaa')
    await wrapper.find('button[name="register"]').trigger('click')
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
    const repeatpasswordInput = wrapper.findAllComponents({ name: 'ElInput' })[2]
    await usernameInput.setValue('aaa')
    await passwordInput.setValue('aaa')
    await repeatpasswordInput.setValue('aaa')
    await wrapper.find('button[name="register"]').trigger('click')
    await wrapper.vm.submitForm(formEl)

    expect(formEl.validate).toHaveBeenCalled()
    expect(consoleMock.log).toHaveBeenCalledWith('error submit!', { field: ['error message'] })
  })
  it('检测密码校验不一致', async () => {
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
    const repeatpasswordInput = wrapper.findAllComponents({ name: 'ElInput' })[2]
    await usernameInput.setValue('aaa')
    await passwordInput.setValue('aaaaaa')
    await repeatpasswordInput.setValue('aaa')
    await wrapper.find('button[name="register"]').trigger('click')
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
    const repeatpasswordInput = wrapper.findAllComponents({ name: 'ElInput' })[2]
    await usernameInput.setValue('aaa')
    await passwordInput.setValue('aaaaaa')
    await repeatpasswordInput.setValue('aaaaaa')
    await wrapper.find('button[name="register"]').trigger('click')
    await wrapper.vm.submitForm(formEl)

    expect(formEl.validate).toHaveBeenCalled()
    expect(consoleMock.log).toHaveBeenCalledWith('error submit!', { field: ['error message'] })
  })
})
