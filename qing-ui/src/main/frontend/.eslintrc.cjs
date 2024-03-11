/* eslint-env node */
// require("@rushstack/eslint-patch/modern-module-resolution");

module.exports = {
  root: true,
  plugins: ["@typescript-eslint"],
  extends: [
    "plugin:vue/vue3-essential",
    "eslint:recommended",
    "@vue/eslint-config-typescript",
    "@vue/eslint-config-prettier",
    "plugin:@typescript-eslint/eslint-recommended",
    "plugin:@typescript-eslint/recommended"
  ],
  rules: {
    "consistent-return": 2,
    "no-else-return": 1,
    semi: [1, "always"],
    "space-unary-ops": 2,
    "wrap-attributes": "off"
  },
  overrides: [
    {
      files: ["cypress/e2e/**/*.{cy,spec}.{js,ts,jsx,tsx}"],
      extends: ["plugin:cypress/recommended"]
    },
    {
      files: ["*.vue", "*.ts"],
      rules: {
        // 给上面匹配的文件指定规则
        "vue/multi-word-component-names": "off"
      }
    }
  ],
  parserOptions: {
    ecmaVersion: "latest",
    parser: "@babel/eslint-parser",
    requireConfigFile: false
  }
};
