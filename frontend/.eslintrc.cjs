module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: ["eslint:recommended", "plugin:@typescript-eslint/recommended", "plugin:vue/vue3-recommended"],
  overrides: [
    {
      env: {
        node: true,
      },
      files: [".eslintrc.{js,cjs}"],
      parserOptions: {
        sourceType: "script",
      },
    },
  ],
  parser: "vue-eslint-parser",
  parserOptions: {
    ecmaVersion: "latest",
    parser: "@typescript-eslint/parser",
    sourceType: "module",
  },
  plugins: ["@typescript-eslint", "vue", "simple-import-sort"],
  rules: {
    "vue/multi-word-component-names": [
      "error",
      {
        ignores: ["index"],
      },
    ],
    "vue/html-closing-bracket-newline": 0,
    "vue/singleline-html-element-content-newline": 0,
    "vue/max-attributes-per-line": [
      "error",
      {
        singleline: {
          max: 7,
        },
        multiline: {
          max: 2,
        },
      },
    ],
    "simple-import-sort/imports": [
      "error",
      {
        groups: [
          [`^vue$`, `^vue-router$`, `^ant-design-vue$`, `^echarts$`], // 你可以根据需要添加更多的内置模块
          [`.*.vue$`], // .vue 文件
          [`.*/assets/.*`, `^@/assets$`],
          [`.*/config/.*`, `^@/config$`],
          [`.*/hooks/.*`, `^@/hooks$`],
          [`.*/plugins/.*`, `^@/plugins$`],
          [`.*/router/.*`, `^@/router$`],
          [`^@/services$`, `^@/services/.*`],
          [`.*/store/.*`, `^@/store$`],
          [`.*/utils/.*`, `^@/utils$`],
          [`^`],
          [`^type `],
        ],
      },
    ],
    "simple-import-sort/exports": "error",
    "vue/html-self-closing": 0,
  },
}
