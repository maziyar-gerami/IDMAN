(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["chunk-284bd316"], { "102f": function (e, t, i) { }, "536d": function (e, t, i) { "use strict"; i("102f") }, aa72: function (e, t, i) { "use strict"; i.r(t); var n = i("7a23"), a = { class: "grid" }, r = { class: "col-12" }, o = { class: "card" }, c = { class: "flex justify-content-center flex-column sm:flex-row" }, s = { class: "text-right" }, l = { class: "text-right" }, d = { class: "flex align-items-center justify-content-center" }; function u(e, t, i, u, b, f) { var v = Object(n["K"])("Button"), m = Object(n["K"])("Toolbar"), p = Object(n["K"])("Paginator"), O = Object(n["K"])("InputText"), g = Object(n["K"])("Column"), h = Object(n["K"])("DataTable"), j = Object(n["L"])("tooltip"); return Object(n["C"])(), Object(n["j"])("div", a, [Object(n["k"])("div", r, [Object(n["k"])("div", o, [Object(n["k"])("h3", null, Object(n["O"])(e.$t("devices")), 1), Object(n["o"])(m, null, { end: Object(n["U"])((function () { return [Object(n["V"])(Object(n["o"])(v, { icon: "pi pi-filter-slash", class: "p-button-danger mb-2 mx-1", onClick: t[0] || (t[0] = function (e) { return f.removeFilters("all") }) }, null, 512), [[j, e.$t("removeFilters"), void 0, { top: !0 }]])] })), _: 1 }), Object(n["o"])(h, { value: b.devices, filterDisplay: "menu", dataKey: "_id", rows: b.rowsPerPage, filters: b.filters, "onUpdate:filters": t[6] || (t[6] = function (e) { return b.filters = e }), loading: b.loading, selection: b.selectedDevices, "onUpdate:selection": t[7] || (t[7] = function (e) { return b.selectedDevices = e }), class: "p-datatable-gridlines", rowHover: !0, responsiveLayout: "scroll", scrollable: !1, scrollHeight: "50vh", scrollDirection: "vertical" }, { header: Object(n["U"])((function () { return [Object(n["k"])("div", c, [Object(n["o"])(p, { rows: b.rowsPerPage, "onUpdate:rows": t[1] || (t[1] = function (e) { return b.rowsPerPage = e }), totalRecords: b.totalRecordsCount, "onUpdate:totalRecords": t[2] || (t[2] = function (e) { return b.totalRecordsCount = e }), onPage: t[3] || (t[3] = function (e) { return f.onPaginatorEvent(e) }), rowsPerPageOptions: [10, 20, 50, 100, 500] }, null, 8, ["rows", "totalRecords"])])] })), empty: Object(n["U"])((function () { return [Object(n["k"])("div", s, Object(n["O"])(e.$t("noDevicesFound")), 1)] })), loading: Object(n["U"])((function () { return [Object(n["k"])("div", l, Object(n["O"])(e.$t("loadingDevices")), 1)] })), default: Object(n["U"])((function () { return [Object(n["o"])(g, { field: "username", header: e.$t("userId"), bodyClass: "text-center", style: { flex: "0 0 10rem" } }, { body: Object(n["U"])((function (e) { var t = e.data; return [Object(n["n"])(Object(n["O"])(t.username), 1)] })), filter: Object(n["U"])((function (i) { var a = i.filterCallback; return [Object(n["o"])(O, { type: "text", modelValue: b.devicesFilters.username, "onUpdate:modelValue": t[4] || (t[4] = function (e) { return b.devicesFilters.username = e }), onKeydown: Object(n["W"])((function (e) { a(), f.devicesRequestMaster("getDevices") }), ["enter"]), class: "p-column-filter", placeholder: e.$t("userId") }, null, 8, ["modelValue", "onKeydown", "placeholder"])] })), filterapply: Object(n["U"])((function (t) { var i = t.filterCallback; return [Object(n["V"])(Object(n["o"])(v, { type: "button", icon: "pi pi-check", onClick: function (e) { i(), f.devicesRequestMaster("getDevices") }, class: "p-button-success" }, null, 8, ["onClick"]), [[j, e.$t("applyFilter"), void 0, { top: !0 }]])] })), filterclear: Object(n["U"])((function (t) { var i = t.filterCallback; return [Object(n["V"])(Object(n["o"])(v, { type: "button", icon: "pi pi-times", onClick: function (e) { i(), f.removeFilters("username") }, class: "p-button-danger" }, null, 8, ["onClick"]), [[j, e.$t("removeFilter"), void 0, { top: !0 }]])] })), _: 1 }, 8, ["header"]), Object(n["o"])(g, { field: "name", header: e.$t("deviceName"), bodyClass: "text-center", style: { flex: "0 0 10rem" } }, { body: Object(n["U"])((function (e) { var t = e.data; return [Object(n["n"])(Object(n["O"])(t.name), 1)] })), filter: Object(n["U"])((function (i) { var a = i.filterCallback; return [Object(n["o"])(O, { type: "text", modelValue: b.devicesFilters.name, "onUpdate:modelValue": t[5] || (t[5] = function (e) { return b.devicesFilters.name = e }), onKeydown: Object(n["W"])((function (e) { a(), f.devicesRequestMaster("getDevices") }), ["enter"]), class: "p-column-filter", placeholder: e.$t("deviceName") }, null, 8, ["modelValue", "onKeydown", "placeholder"])] })), filterapply: Object(n["U"])((function (t) { var i = t.filterCallback; return [Object(n["V"])(Object(n["o"])(v, { type: "button", icon: "pi pi-check", onClick: function (e) { i(), f.devicesRequestMaster("getDevices") }, class: "p-button-success" }, null, 8, ["onClick"]), [[j, e.$t("applyFilter"), void 0, { top: !0 }]])] })), filterclear: Object(n["U"])((function (t) { var i = t.filterCallback; return [Object(n["V"])(Object(n["o"])(v, { type: "button", icon: "pi pi-times", onClick: function (e) { i(), f.removeFilters("name") }, class: "p-button-danger" }, null, 8, ["onClick"]), [[j, e.$t("removeFilter"), void 0, { top: !0 }]])] })), _: 1 }, 8, ["header"]), Object(n["o"])(g, { field: "dateString", header: e.$t("date"), bodyClass: "text-center", style: { flex: "0 0 7rem" } }, { body: Object(n["U"])((function (e) { var t = e.data; return [Object(n["n"])(Object(n["O"])(t.dateString), 1)] })), _: 1 }, 8, ["header"]), Object(n["o"])(g, { field: "timeString", header: e.$t("time"), bodyClass: "text-center", style: { flex: "0 0 7rem" } }, { body: Object(n["U"])((function (e) { var t = e.data; return [Object(n["n"])(Object(n["O"])(t.timeString), 1)] })), _: 1 }, 8, ["header"]), Object(n["o"])(g, { bodyStyle: "display: flex;", bodyClass: "flex justify-content-evenly flex-wrap card-container text-center w-full", style: { flex: "0 0 5rem" } }, { body: Object(n["U"])((function (t) { var i = t.data; return [Object(n["k"])("div", d, [Object(n["V"])(Object(n["o"])(v, { icon: "bx bx-trash bx-sm", class: "p-button-rounded p-button-danger p-button-outlined mx-1", onClick: function (e) { return f.deleteDevice(i.name) } }, null, 8, ["onClick"]), [[j, e.$t("deleteDevice"), void 0, { top: !0 }]])])] })), _: 1 })] })), _: 1 }, 8, ["value", "rows", "filters", "loading", "selection"])])])]) } var b = i("0393"), f = i("e6da"), v = i.n(f), m = { name: "Devices", data: function () { return { devices: [], selectedDevices: [], devicesFilters: { username: "", name: "" }, deviceToolbarBuffer: "", rowsPerPage: 20, newPageNumber: 1, totalRecordsCount: 20, loading: !0, filters: null } }, mounted: function () { this.initiateFilters(), this.devicesRequestMaster("getDevices") }, methods: { initiateFilters: function () { this.filters = { username: { value: null, matchMode: b["a"].CONTAINS }, name: { value: null, matchMode: b["a"].CONTAINS } } }, onPaginatorEvent: function (e) { this.newPageNumber = e.page + 1, this.rowsPerPage = e.rows, this.devicesRequestMaster("getDevices") }, devicesRequestMaster: function (e) { var t = this, i = ""; "Fa" === this.$i18n.locale ? i = "fa" : "En" === this.$i18n.locale && (i = "en"), "getDevices" === e ? (this.loading = !0, this.axios({ url: "/api/googleAuth", method: "GET", params: { username: t.devicesFilters.username, deviceName: t.devicesFilters.name, page: String(t.newPageNumber), count: String(t.rowsPerPage), lang: i } }).then((function (e) { if (200 === e.data.status.code) { for (var i in e.data.data.devices) e.data.data.devices[i].dateString = e.data.data.devices[i].time.year + "/" + e.data.data.devices[i].time.month + "/" + e.data.data.devices[i].time.day, e.data.data.devices[i].timeString = e.data.data.devices[i].time.hours + ":" + e.data.data.devices[i].time.minutes + ":" + e.data.data.devices[i].time.seconds; t.devices = e.data.data.devices, t.totalRecordsCount = e.data.data.size, t.loading = !1 } else t.alertPromptMaster(t.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), t.loading = !1 })).catch((function () { t.alertPromptMaster(t.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), t.loading = !1 }))) : "deleteDevice" === e && (this.loading = !0, this.axios({ url: "/api/googleAuth", method: "DELETE", params: { deviceName: t.deviceToolbarBuffer, lang: i } }).then((function (e) { 204 === e.data.status.code ? (t.loading = !1, t.devicesRequestMaster("getDevices")) : 403 === e.data.status.code ? (t.alertPromptMaster(e.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA"), t.loading = !1) : (t.alertPromptMaster(t.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), t.loading = !1) })).catch((function () { t.alertPromptMaster(t.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), t.loading = !1 }))) }, alertPromptMaster: function (e, t, i, n) { var a = !0; "ltr" === this.$store.state.direction && (a = !1), v.a.show({ title: e, message: t, position: "center", icon: "pi " + i, backgroundColor: n, transitionIn: "fadeInLeft", rtl: a, layout: 2, timeout: !1, progressBar: !1, buttons: [["<button class='service-notification-button my-3' style='border-radius: 6px;'>" + this.$t("confirm") + "</button>", function (e, t) { e.hide({ transitionOut: "fadeOutRight" }, t) }]] }) }, confirmPromptMaster: function (e, t, i, n, a, r) { var o = !0; "ltr" === this.$store.state.direction && (o = !1), v.a.show({ title: e, message: t, position: "center", icon: "pi " + i, backgroundColor: n, transitionIn: "fadeInLeft", rtl: o, layout: 2, timeout: !1, progressBar: !1, buttons: [["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>" + this.$t("yes") + "</button>", function (e, t) { e.hide({ transitionOut: "fadeOutRight" }, t), a(r) }], ["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>" + this.$t("no") + "</button>", function (e, t) { e.hide({ transitionOut: "fadeOutRight" }, t) }]] }) }, deleteDevice: function (e) { this.deviceToolbarBuffer = e, this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("deleteDevice") + "" + String(e), "pi-question-circle", "#F0EAAA", this.devicesRequestMaster, "deleteDevice") }, removeFilters: function (e) { "username" === e ? this.devicesFilters.username = "" : "name" === e ? this.devicesFilters.name = "" : "all" === e && (this.devicesFilters.username = "", this.devicesFilters.name = ""), this.devicesRequestMaster("getDevices") } } }, p = (i("536d"), i("6b0d")), O = i.n(p); const g = O()(m, [["render", u], ["__scopeId", "data-v-18e589b8"]]); t["default"] = g } }]);
//# sourceMappingURL=chunk-284bd316.d44567f2.js.map