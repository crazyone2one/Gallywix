module.exports = {
    "env": {
        "browser": true,
        "es2021": true,
        "node": true
    },
    "extends": [
        "eslint:recommended",
        "plugin:@typescript-eslint/recommended",
        "plugin:vue/vue3-recommended"
    ],
    "overrides": [
        {
            "env": {
                "node": true
            },
            "files": [
                ".eslintrc.{js,cjs}"
            ],
            "parserOptions": {
                "sourceType": "script"
            }
        }
    ],
    "parser": "vue-eslint-parser",
    "parserOptions": {
        "ecmaVersion": "latest",
        "parser": "@typescript-eslint/parser",
        "sourceType": "module"
    },
    "plugins": [
        "@typescript-eslint",
        "vue"
    ],
    "rules": {
        "vue/singleline-html-element-content-newline": ["error", {
          "ignoreWhenNoAttributes": true,
          "ignoreWhenEmpty": true,
          "ignores": ["pre", "textarea", ...INLINE_ELEMENTS]
        }],
        "vue/max-attributes-per-line": ["error", {
            "singleline": {
              "max": 4
            },      
            "multiline": {
              "max": 1
            }
          }]
      }
}
