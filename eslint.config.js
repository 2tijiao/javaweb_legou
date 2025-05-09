import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'
import globals from 'globals'

export default [
  {
    name: 'app/files-to-lint',
    files: ['**/*.{js,mjs,jsx,vue}'],
  },

  {
    name: 'app/files-to-ignore',
    ignores: ['**/dist/**', '**/dist-ssr/**', '**/coverage/**'],
  },

  {
    languageOptions: {
      globals: {
        ...globals.browser,
      },
    },
  },

  // eslint起名规范比较严格，这样可以不要求起名必须是a-b类型
  {
    rules: {
      'vue/multi-word-component-names': off
    }
  },

  js.configs.recommended,
  ...pluginVue.configs['flat/essential'],
]
