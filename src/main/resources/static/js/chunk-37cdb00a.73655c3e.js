(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["chunk-37cdb00a"], { "8b16": function (t, e, a) { "use strict"; a("a5bb") }, a5bb: function (t, e, a) { }, f50e: function (t, e, a) { "use strict"; a.r(e); var s = a("7a23"), r = { class: "grid" }, o = { class: "col-12" }, i = { class: "card" }, n = { class: "flex justify-content-center flex-column sm:flex-row" }, l = { class: "text-right" }, c = { class: "text-right" }, u = { class: "fa-stack fa-lg" }; function d(t, e, a, d, p, b) { var f = Object(s["K"])("Dropdown"), m = Object(s["K"])("Button"), h = Object(s["K"])("Toolbar"), O = Object(s["K"])("Paginator"), g = Object(s["K"])("Column"), j = Object(s["K"])("InputText"), y = Object(s["K"])("DataTable"), v = Object(s["L"])("tooltip"); return Object(s["C"])(), Object(s["j"])("div", r, [Object(s["k"])("div", o, [Object(s["k"])("div", i, [Object(s["k"])("h3", null, Object(s["O"])(t.$t("roles")), 1), Object(s["o"])(h, null, { start: Object(s["U"])((function () { return [Object(s["o"])(f, { modelValue: p.selectedRoleOption, "onUpdate:modelValue": e[0] || (e[0] = function (t) { return p.selectedRoleOption = t }), options: p.roleOptions, optionLabel: "name", placeholder: t.$t("chooseRole") }, null, 8, ["modelValue", "options", "placeholder"]), Object(s["o"])(m, { label: t.$t("changeRole"), class: "p-button-success mx-1", onClick: e[1] || (e[1] = function (t) { return b.changeRole() }) }, null, 8, ["label"])] })), end: Object(s["U"])((function () { return [Object(s["V"])(Object(s["o"])(m, { icon: "pi pi-filter-slash", class: "p-button-danger mb-2 mx-1", onClick: e[2] || (e[2] = function (t) { return b.removeFilters("all") }) }, null, 512), [[v, t.$t("removeFilters"), void 0, { top: !0 }]])] })), _: 1 }), Object(s["o"])(y, { value: p.users, filterDisplay: "menu", dataKey: "_id", rows: p.rowsPerPage, filters: p.filters, "onUpdate:filters": e[8] || (e[8] = function (t) { return p.filters = t }), loading: p.loading, selection: p.selectedUsers, "onUpdate:selection": e[9] || (e[9] = function (t) { return p.selectedUsers = t }), class: "p-datatable-gridlines", rowHover: !0, responsiveLayout: "scroll", scrollable: !1, scrollHeight: "50vh", scrollDirection: "vertical" }, { header: Object(s["U"])((function () { return [Object(s["k"])("div", n, [Object(s["o"])(O, { rows: p.rowsPerPage, "onUpdate:rows": e[3] || (e[3] = function (t) { return p.rowsPerPage = t }), totalRecords: p.totalRecordsCount, "onUpdate:totalRecords": e[4] || (e[4] = function (t) { return p.totalRecordsCount = t }), onPage: e[5] || (e[5] = function (t) { return b.onPaginatorEvent(t) }), rowsPerPageOptions: [10, 20, 50, 100, 500] }, null, 8, ["rows", "totalRecords"])])] })), empty: Object(s["U"])((function () { return [Object(s["k"])("div", l, Object(s["O"])(t.$t("noUsersFound")), 1)] })), loading: Object(s["U"])((function () { return [Object(s["k"])("div", c, Object(s["O"])(t.$t("loadingUsers")), 1)] })), default: Object(s["U"])((function () { return [Object(s["o"])(g, { selectionMode: "multiple", bodyClass: "text-center", style: { width: "5rem" } }), Object(s["o"])(g, { bodyClass: "text-center", style: { width: "5rem" } }, { body: Object(s["U"])((function (t) { var e = t.data; return [Object(s["k"])("span", u, [Object(s["k"])("i", { class: "fa fa-user fa-lg", style: Object(s["x"])(e.icon) }, null, 4)])] })), _: 1 }), Object(s["o"])(g, { field: "_id", header: t.$t("id"), bodyClass: "text-center", style: { flex: "0 0 10rem" } }, { body: Object(s["U"])((function (t) { var e = t.data; return [Object(s["n"])(Object(s["O"])(e._id), 1)] })), filter: Object(s["U"])((function (a) { var r = a.filterCallback; return [Object(s["o"])(j, { type: "text", modelValue: p.usersFilters._id, "onUpdate:modelValue": e[6] || (e[6] = function (t) { return p.usersFilters._id = t }), onKeydown: Object(s["W"])((function (t) { r(), b.rolesRequestMaster("getUsers") }), ["enter"]), class: "p-column-filter", placeholder: t.$t("id") }, null, 8, ["modelValue", "onKeydown", "placeholder"])] })), filterapply: Object(s["U"])((function (e) { var a = e.filterCallback; return [Object(s["V"])(Object(s["o"])(m, { type: "button", icon: "pi pi-check", onClick: function (t) { a(), b.rolesRequestMaster("getUsers") }, class: "p-button-success" }, null, 8, ["onClick"]), [[v, t.$t("applyFilter"), void 0, { top: !0 }]])] })), filterclear: Object(s["U"])((function (e) { var a = e.filterCallback; return [Object(s["V"])(Object(s["o"])(m, { type: "button", icon: "pi pi-times", onClick: function (t) { a(), b.removeFilters("_id") }, class: "p-button-danger" }, null, 8, ["onClick"]), [[v, t.$t("removeFilter"), void 0, { top: !0 }]])] })), _: 1 }, 8, ["header"]), Object(s["o"])(g, { field: "role", header: t.$t("role"), bodyClass: "text-center", style: { flex: "0 0 10rem" } }, { body: Object(s["U"])((function (t) { var e = t.data; return [Object(s["n"])(Object(s["O"])(e.roleFa), 1)] })), _: 1 }, 8, ["header"]), Object(s["o"])(g, { field: "displayName", header: t.$t("persianName"), bodyClass: "text-center", style: { flex: "0 0 15rem" } }, { body: Object(s["U"])((function (t) { var e = t.data; return [Object(s["n"])(Object(s["O"])(e.displayName), 1)] })), filter: Object(s["U"])((function (a) { var r = a.filterCallback; return [Object(s["o"])(j, { type: "text", modelValue: p.usersFilters.displayName, "onUpdate:modelValue": e[7] || (e[7] = function (t) { return p.usersFilters.displayName = t }), onKeydown: Object(s["W"])((function (t) { r(), b.rolesRequestMaster("getUsers") }), ["enter"]), class: "p-column-filter", placeholder: t.$t("persianName") }, null, 8, ["modelValue", "onKeydown", "placeholder"])] })), filterapply: Object(s["U"])((function (e) { var a = e.filterCallback; return [Object(s["V"])(Object(s["o"])(m, { type: "button", icon: "pi pi-check", onClick: function (t) { a(), b.rolesRequestMaster("getUsers") }, class: "p-button-success" }, null, 8, ["onClick"]), [[v, t.$t("applyFilter"), void 0, { top: !0 }]])] })), filterclear: Object(s["U"])((function (e) { var a = e.filterCallback; return [Object(s["V"])(Object(s["o"])(m, { type: "button", icon: "pi pi-times", onClick: function (t) { a(), b.removeFilters("displayName") }, class: "p-button-danger" }, null, 8, ["onClick"]), [[v, t.$t("removeFilter"), void 0, { top: !0 }]])] })), _: 1 }, 8, ["header"])] })), _: 1 }, 8, ["value", "rows", "filters", "loading", "selection"])])])]) } var p = a("0393"), b = a("e6da"), f = a.n(b), m = { name: "Roles", data: function () { return { users: [], selectedUsers: [], usersFilters: { _id: "", displayName: "" }, roleOptions: [{ id: "SUPERUSER", name: this.$t("superUser") }, { id: "SUPPORTER", name: this.$t("supporter") }, { id: "ADMIN", name: this.$t("admin") }, { id: "PRESENTER", name: this.$t("presenter") }, { id: "USER", name: this.$t("user") }], selectedRoleOption: {}, rowsPerPage: 20, newPageNumber: 1, totalRecordsCount: 20, loading: !0, filters: null } }, mounted: function () { this.initiateFilters(), this.rolesRequestMaster("getUsers") }, methods: { initiateFilters: function () { this.filters = { _id: { value: null, matchMode: p["a"].CONTAINS }, displayName: { value: null, matchMode: p["a"].CONTAINS } } }, onPaginatorEvent: function (t) { this.newPageNumber = t.page + 1, this.rowsPerPage = t.rows, this.rolesRequestMaster("getUsers") }, rolesRequestMaster: function (t) { var e = this, a = ""; if ("Fa" === this.$i18n.locale ? a = "fa" : "En" === this.$i18n.locale && (a = "en"), "getUsers" === t) { var s = [], r = [], o = [], i = [], n = []; this.loading = !0, this.axios({ url: "/api/users", method: "GET", params: { userId: e.usersFilters._id, displayName: e.usersFilters.displayName, sort: "role", page: String(e.newPageNumber), count: String(e.rowsPerPage), lang: a } }).then((function (t) { if (200 === t.data.status.code) { for (var a in t.data.data.userList) "SUPERUSER" === t.data.data.userList[a].role ? (t.data.data.userList[a].roleFa = "مدیر کل", t.data.data.userList[a].icon = "color: #dc3545;", s.push(t.data.data.userList[a])) : "SUPPORTER" === t.data.data.userList[a].role ? (t.data.data.userList[a].roleFa = "پشتیبانی", t.data.data.userList[a].icon = "color: #28a745;", r.push(t.data.data.userList[a])) : "ADMIN" === t.data.data.userList[a].role ? (t.data.data.userList[a].roleFa = "مدیر", t.data.data.userList[a].icon = "color: #007bff;", o.push(t.data.data.userList[a])) : "PRESENTER" === t.data.data.userList[a].role ? (t.data.data.userList[a].roleFa = "ارائه دهنده", t.data.data.userList[a].icon = "color: #00f7f7;", i.push(t.data.data.userList[a])) : "USER" === t.data.data.userList[a].role && (t.data.data.userList[a].roleFa = "کاربر", t.data.data.userList[a].icon = "color: #ffc107;", n.push(t.data.data.userList[a])); e.users = s.concat(r, o, i, n), e.totalRecordsCount = t.data.data.size, e.loading = !1 } else e.alertPromptMaster(e.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), e.loading = !1 })).catch((function () { e.alertPromptMaster(e.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), e.loading = !1 })) } else if ("editRole" === t) { var l = []; for (var c in this.selectedUsers) l.push(this.selectedUsers[c]._id); this.loading = !0, this.axios({ url: "/api/roles/" + e.selectedRoleOption.id, method: "PUT", headers: { "Content-Type": "application/json" }, params: { lang: a }, data: JSON.stringify({ names: l }).replace(/\\\\/g, "\\") }).then((function (t) { 200 === t.data.status.code ? (e.loading = !1, e.rolesRequestMaster("getUsers")) : 206 === t.data.status.code ? (e.alertPromptMaster(t.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA"), e.loading = !1, e.rolesRequestMaster("getUsers")) : (e.alertPromptMaster(e.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), e.loading = !1) })).catch((function () { e.alertPromptMaster(e.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA"), e.loading = !1 })) } }, alertPromptMaster: function (t, e, a, s) { var r = !0; "ltr" === this.$store.state.direction && (r = !1), f.a.show({ title: t, message: e, position: "center", icon: "pi " + a, backgroundColor: s, transitionIn: "fadeInLeft", rtl: r, layout: 2, timeout: !1, progressBar: !1, buttons: [["<button class='service-notification-button my-3' style='border-radius: 6px;'>" + this.$t("confirm") + "</button>", function (t, e) { t.hide({ transitionOut: "fadeOutRight" }, e) }]] }) }, confirmPromptMaster: function (t, e, a, s, r, o) { var i = !0; "ltr" === this.$store.state.direction && (i = !1), f.a.show({ title: t, message: e, position: "center", icon: "pi " + a, backgroundColor: s, transitionIn: "fadeInLeft", rtl: i, layout: 2, timeout: !1, progressBar: !1, buttons: [["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>" + this.$t("yes") + "</button>", function (t, e) { t.hide({ transitionOut: "fadeOutRight" }, e), r(o) }], ["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>" + this.$t("no") + "</button>", function (t, e) { t.hide({ transitionOut: "fadeOutRight" }, e) }]] }) }, changeRole: function () { this.selectedUsers.length > 0 ? 0 !== Object.keys(this.selectedRoleOption).length ? this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("changeRole") + " " + this.$t("users"), "pi-question-circle", "#F0EAAA", this.rolesRequestMaster, "editRole") : this.alertPromptMaster(this.$t("noRoleSelected"), "", "pi-exclamation-triangle", "#FDB5BA") : this.alertPromptMaster(this.$t("noUserSelected"), "", "pi-exclamation-triangle", "#FDB5BA") }, removeFilters: function (t) { "_id" === t ? this.usersFilters._id = "" : "displayName" === t ? this.usersFilters.displayName = "" : "all" === t && (this.usersFilters._id = "", this.usersFilters.displayName = ""), this.rolesRequestMaster("getUsers") } } }, h = (a("8b16"), a("6b0d")), O = a.n(h); const g = O()(m, [["render", d], ["__scopeId", "data-v-78cdd28f"]]); e["default"] = g } }]);
//# sourceMappingURL=chunk-37cdb00a.73655c3e.js.map