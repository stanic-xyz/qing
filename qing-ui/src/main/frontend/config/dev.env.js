"use strict";
import {merge} from "webpack-merge";
// const merge = require("webpack-merge");
// const prodEnv = require("./prod.env");
import "./prod.env";

let baseUrl = "http://localhost:8080/";

module.exports = merge(prodEnv, {
    NODE_ENV: '"development"',
    baseUrl: baseUrl,
    BASE_URL: baseUrl,
});
