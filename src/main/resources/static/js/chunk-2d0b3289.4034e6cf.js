(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0b3289"],{"26d3":function(t,e,a){"use strict";a.r(e);var s=a("7a23"),i={class:"grid"},r={class:"col-12"},n={class:"card"},o={key:0,class:"text-center"},l={key:1},c={class:"p-inputgroup mx-1"},u={class:"formgrid grid"},d={class:"field p-fluid"},p=["for"],g={class:"flex align-items-center justify-content-center text-white border-circle mx-2",style:{"background-color":"#007bff"}},h=Object(s["k"])("i",{class:"bx bx-question-mark bx-sm"},null,-1),b=[h],m=["dir"];function f(t,e,a,h,f,O){var j=Object(s["K"])("ProgressSpinner"),v=Object(s["K"])("Dropdown"),x=Object(s["K"])("Button"),k=Object(s["K"])("OverlayPanel"),B=Object(s["K"])("Toolbar"),$=Object(s["K"])("InputText"),y=Object(s["K"])("InputNumber"),M=Object(s["K"])("InputSwitch"),A=Object(s["K"])("TabPanel"),q=Object(s["K"])("TabView"),F=Object(s["L"])("tooltip");return Object(s["C"])(),Object(s["j"])("div",i,[Object(s["k"])("div",r,[Object(s["k"])("div",n,[Object(s["k"])("h3",null,Object(s["O"])(t.$t("settings")),1),f.loading?(Object(s["C"])(),Object(s["j"])("div",o,[Object(s["o"])(j)])):(Object(s["C"])(),Object(s["j"])("div",l,[Object(s["o"])(B,null,{start:Object(s["U"])((function(){return[Object(s["o"])(v,{modelValue:f.selectedBackup,"onUpdate:modelValue":e[0]||(e[0]=function(t){return f.selectedBackup=t}),options:f.backupList,optionLabel:"name",placeholder:t.$t("chooseBackup")},null,8,["modelValue","options","placeholder"]),Object(s["V"])(Object(s["o"])(x,{icon:"bx bx-revision bx-sm",class:"p-button mx-1",onClick:e[1]||(e[1]=function(t){return O.backupHelper("restoreBackup")})},null,512),[[F,t.$t("restore"),void 0,{top:!0}]])]})),end:Object(s["U"])((function(){return[Object(s["V"])(Object(s["o"])(x,{icon:"bx bx-save bx-sm",class:"p-button mx-1",onClick:e[2]||(e[2]=function(t){return O.settingsRequestMaster("getBackup")})},null,512),[[F,t.$t("getBackup"),void 0,{top:!0}]]),Object(s["V"])(Object(s["o"])(x,{icon:"bx bx-sync bx-sm",class:"p-button-warning mx-1",onClick:e[3]||(e[3]=function(t){return O.toggleRefresh(t)})},null,512),[[F,t.$t("refresh"),void 0,{top:!0}]]),Object(s["o"])(k,{ref:"refresh"},{default:Object(s["U"])((function(){return[Object(s["k"])("div",c,[Object(s["V"])(Object(s["o"])(x,{icon:"bx bx-sync bx-sm",onClick:e[4]||(e[4]=function(t){return O.refreshHelper()})},null,512),[[F,t.$t("refresh"),void 0,{top:!0}]]),Object(s["o"])(v,{modelValue:f.selectedRefresh,"onUpdate:modelValue":e[5]||(e[5]=function(t){return f.selectedRefresh=t}),options:f.refreshList,optionLabel:"name",placeholder:t.$t("select")},null,8,["modelValue","options","placeholder"])])]})),_:1},512),Object(s["V"])(Object(s["o"])(x,{icon:"bx bx-reset bx-sm",class:"p-button-danger mx-1",onClick:e[6]||(e[6]=function(t){return O.backupHelper("reset")})},null,512),[[F,t.$t("reset"),void 0,{top:!0}]])]})),_:1}),Object(s["o"])(q,{activeIndex:f.tabActiveIndex,"onUpdate:activeIndex":e[7]||(e[7]=function(t){return f.tabActiveIndex=t})},{default:Object(s["U"])((function(){return[(Object(s["C"])(!0),Object(s["j"])(s["a"],null,Object(s["I"])(f.settings,(function(e,a){return Object(s["C"])(),Object(s["h"])(A,{key:a,header:e[0].group},{default:Object(s["U"])((function(){return[Object(s["k"])("div",u,[(Object(s["C"])(!0),Object(s["j"])(s["a"],null,Object(s["I"])(e,(function(e){return Object(s["C"])(),Object(s["j"])("div",{class:"field col-6",key:e._id},[Object(s["k"])("div",d,[Object(s["k"])("label",{for:e._id,class:"flex"},[Object(s["n"])(Object(s["O"])(e.description)+" ",1),Object(s["V"])((Object(s["C"])(),Object(s["j"])("span",g,b)),[[F,e.help,void 0,{top:!0}]])],8,p),"string"===e.type.class||"path"===e.type.class||"url"===e.type.class?(Object(s["C"])(),Object(s["h"])($,{key:0,id:e._id,type:"text",modelValue:e.value,"onUpdate:modelValue":function(t){return e.value=t}},null,8,["id","modelValue","onUpdate:modelValue"])):"integer"===e.type.class?(Object(s["C"])(),Object(s["j"])("div",{key:1,dir:t.$store.state.reverseDirection},[Object(s["o"])(y,{id:e._id,modelValue:e.value,"onUpdate:modelValue":function(t){return e.value=t},showButtons:"",mode:"decimal"},null,8,["id","modelValue","onUpdate:modelValue"])],8,m)):"switch"===e.type.class?(Object(s["C"])(),Object(s["h"])(M,{key:2,modelValue:e.value,"onUpdate:modelValue":function(t){return e.value=t},id:e._id},null,8,["modelValue","onUpdate:modelValue","id"])):"list"===e.type.class?(Object(s["C"])(),Object(s["h"])(v,{key:3,modelValue:e.value,"onUpdate:modelValue":function(t){return e.value=t},id:e._id,options:e.type.values,placeholder:e.value},null,8,["modelValue","onUpdate:modelValue","id","options","placeholder"])):Object(s["i"])("",!0)])])})),128))])]})),_:2},1032,["header"])})),128))]})),_:1},8,["activeIndex"]),Object(s["o"])(x,{label:t.$t("confirm"),class:"p-button-success mt-3 mx-1",onClick:e[8]||(e[8]=function(t){return O.backupHelper("editSettings")})},null,8,["label"])]))])])])}var O=a("e6da"),j=a.n(O),v={name:"Settings",data:function(){return{settings:[],refreshList:[{value:"services",name:this.$t("refreshServices")},{value:"users",name:this.$t("refreshUsers")},{value:"captcha",name:this.$t("refreshCAPTCHA")},{value:"",name:this.$t("refreshAll")}],backupList:[],selectedBackup:{},selectedRefresh:{},tabActiveIndex:0,loading:!1}},mounted:function(){this.settingsRequestMaster("getSettings"),this.settingsRequestMaster("getBackupList")},watch:{"$i18n.locale":{handler:function(){this.settingsRequestMaster("getSettings"),this.settingsRequestMaster("getBackupList")},deep:!0}},methods:{settingsRequestMaster:function(t){var e=this,a="";if("Fa"===this.$i18n.locale?a="fa":"En"===this.$i18n.locale&&(a="en"),"getSettings"===t){var s=[];this.settings=[],this.loading=!0,this.axios({url:"/api/properties/settings",method:"GET",params:{lang:a}}).then((function(t){if(200===t.data.status.code){for(var a in t.data.data)"switch"===t.data.data[a].type.class?"true"===t.data.data[a].value?t.data.data[a].value=!0:"false"===t.data.data[a].value&&(t.data.data[a].value=!1):"integer"===t.data.data[a].type.class&&(t.data.data[a].value=parseInt(t.data.data[a].value)),-1===s.indexOf(t.data.data[a].group)?(s.push(t.data.data[a].group),e.settings.push([t.data.data[a]])):e.settings[s.indexOf(t.data.data[a].group)].push(t.data.data[a]);e.loading=!1}else e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))}else if("editSettings"===t){var i=[];for(var r in this.settings)for(var n in this.settings[r])"switch"===this.settings[r][n].type.class?this.settings[r][n].value?this.settings[r][n].value="true":this.settings[r][n].value="false":"integer"===this.settings[r][n].type.class&&(this.settings[r][n].value=this.settings[r][n].value.toString()),i.push({_id:this.settings[r][n]._id,value:this.settings[r][n].value});this.loading=!0,this.axios({url:"/api/properties/settings",method:"PUT",headers:{"Content-Type":"application/json"},params:{lang:a},data:JSON.stringify(i).replace(/\\\\/g,"\\")}).then((function(t){200===t.data.status.code?(e.loading=!1,e.settingsRequestMaster("getSettings")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1,e.settingsRequestMaster("getSettings"))})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1,e.settingsRequestMaster("getSettings")}))}else"getBackupList"===t?(this.loading=!0,this.axios({url:"/api/properties/settings/backup",method:"GET",params:{lang:a}}).then((function(t){200===t.data.status.code?(e.backupList=t.data.data,e.loading=!1):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))):"getBackup"===t?(this.loading=!0,this.axios({url:"/api/properties/settings/backup",method:"POST",params:{lang:a}}).then((function(t){200===t.data.status.code?(e.loading=!1,e.settingsRequestMaster("getBackupList")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))):"restoreBackup"===t?(this.loading=!0,this.axios({url:"/api/properties/settings/restore",method:"GET",params:{id:e.selectedBackup._id,lang:a}}).then((function(t){200===t.data.status.code?(e.loading=!1,e.settingsRequestMaster("getSettings")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))):"reset"===t?(this.loading=!0,this.axios({url:"/api/properties/settings/reset",method:"GET",params:{lang:a}}).then((function(t){200===t.data.status.code?(e.loading=!1,e.settingsRequestMaster("getSettings")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))):"refresh"===t&&(this.loading=!0,this.axios({url:"/api/refresh",method:"GET",params:{type:e.selectedRefresh.value,lang:a}}).then((function(t){"undefined"!==typeof t.data.status?200===t.data.status.code?e.alertPromptMaster(t.data.status.result,"","pi-check-circle","#A2E1B1"):e.alertPromptMaster(t.data.status.result,"","pi-exclamation-triangle","#FDB5BA"):e.alertPromptMaster(e.$t("requestError"),"","pi-check-circle","#FDB5BA"),e.loading=!1})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-check-circle","#FDB5BA"),e.loading=!1})))},alertPromptMaster:function(t,e,a,s){var i=!0;"ltr"===this.$store.state.direction&&(i=!1),j.a.show({title:t,message:e,position:"center",icon:"pi "+a,backgroundColor:s,transitionIn:"fadeInLeft",rtl:i,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button my-3' style='border-radius: 6px;'>"+this.$t("confirm")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e)}]]})},confirmPromptMaster:function(t,e,a,s,i,r){var n=!0;"ltr"===this.$store.state.direction&&(n=!1),j.a.show({title:t,message:e,position:"center",icon:"pi "+a,backgroundColor:s,transitionIn:"fadeInLeft",rtl:n,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>"+this.$t("yes")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e),i(r)}],["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>"+this.$t("no")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e)}]]})},backupHelper:function(t){"editSettings"===t?this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("edit")+" "+this.$t("settings"),"pi-question-circle","#F0EAAA",this.settingsRequestMaster,"editSettings"):"restoreBackup"===t?0!==Object.keys(this.selectedBackup).length?this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("restore")+" "+this.$t("backup"),"pi-question-circle","#F0EAAA",this.settingsRequestMaster,"restoreBackup"):this.alertPromptMaster(this.$t("noBackupSelected"),"","pi-exclamation-triangle","#FDB5BA"):"reset"===t&&this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("reset"),"pi-question-circle","#F0EAAA",this.settingsRequestMaster,"reset")},refreshHelper:function(){0!==Object.keys(this.selectedRefresh).length?this.confirmPromptMaster(this.$t("confirmPromptText"),this.selectedRefresh.name,"pi-question-circle","#F0EAAA",this.settingsRequestMaster,"refresh"):this.alertPromptMaster(this.$t("noRefreshSelected"),"","pi-exclamation-triangle","#FDB5BA")},toggleRefresh:function(t){this.$refs.refresh.toggle(t)}}},x=a("6b0d"),k=a.n(x);const B=k()(v,[["render",f]]);e["default"]=B}}]);
//# sourceMappingURL=chunk-2d0b3289.4034e6cf.js.map