(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7939ddb1"],{"35bd":function(t,e,i){},"7a42":function(t,e,i){"use strict";i("35bd")},fda7:function(t,e,i){"use strict";i.r(e);var o=i("7a23"),a=function(t){return Object(o["F"])("data-v-4749e7fe"),t=t(),Object(o["D"])(),t},c={class:"grid"},n={class:"col-12"},r={class:"card"},l={class:"text-right"},s={class:"text-right"},d={key:0,class:"bx bxs-check-circle bx-md",style:{color:"#1ca70a"}},f={key:1,class:"bx bxs-x-circle bx-md",style:{color:"#dc3545"}},u={class:"flex align-items-center justify-content-center"},b={class:"flex align-items-center justify-content-center"},m={key:0,class:"text-center"},p={key:1},O={class:"formgrid grid"},j={class:"field col"},N={class:"field p-fluid"},g={for:"createNotification.title"},h=a((function(){return Object(o["k"])("span",{style:{color:"red"}}," * ",-1)})),v={class:"field col"},B={class:"field p-fluid"},y={for:"createNotification.visible",class:"flex"},x={class:"formgrid grid"},k={class:"field col"},T={class:"field p-fluid"},$={for:"createNotification.body"},F={key:0,class:"text-center"},D={key:1},S={class:"formgrid grid"},C={class:"field col"},M={class:"field p-fluid"},E={for:"editNotification.messageId"},V={class:"formgrid grid"},I={class:"field col"},A={class:"field p-fluid"},P={for:"editNotification.creator"},U={class:"field col"},q={class:"field p-fluid"},L={for:"editNotification.updater"},w={class:"formgrid grid"},R={class:"field col"},_={class:"field p-fluid"},K={for:"editNotification.createDateTime"},J={class:"field col"},G={class:"field p-fluid"},H={for:"editNotification.editDateTime"},z={class:"formgrid grid"},Q={class:"field col"},W={class:"field p-fluid"},X={for:"editNotification.title"},Y=a((function(){return Object(o["k"])("span",{style:{color:"red"}}," * ",-1)})),Z={class:"field col"},tt={class:"field p-fluid"},et={for:"editNotification.visible",class:"flex"},it={class:"formgrid grid"},ot={class:"field col"},at={class:"field p-fluid"},ct={for:"editNotification.body"};function nt(t,e,i,a,nt,rt){var lt=Object(o["K"])("Button"),st=Object(o["K"])("Toolbar"),dt=Object(o["K"])("Column"),ft=Object(o["K"])("DataTable"),ut=Object(o["K"])("TabPanel"),bt=Object(o["K"])("ProgressSpinner"),mt=Object(o["K"])("InputText"),pt=Object(o["K"])("InputSwitch"),Ot=Object(o["K"])("Editor"),jt=Object(o["K"])("TabView"),Nt=Object(o["L"])("tooltip");return Object(o["C"])(),Object(o["j"])("div",c,[Object(o["k"])("div",n,[Object(o["k"])("div",r,[Object(o["k"])("h3",null,Object(o["O"])(t.$t("notifications")),1),Object(o["o"])(jt,{activeIndex:nt.tabActiveIndex,"onUpdate:activeIndex":e[18]||(e[18]=function(t){return nt.tabActiveIndex=t})},{default:Object(o["U"])((function(){return[Object(o["o"])(ut,{header:t.$t("notificationsList")},{default:Object(o["U"])((function(){return[Object(o["o"])(st,null,{start:Object(o["U"])((function(){return[Object(o["o"])(lt,{label:t.$t("new"),icon:"pi pi-plus",class:"p-button-success mx-1",onClick:e[0]||(e[0]=function(t){return rt.createNotification()})},null,8,["label"]),Object(o["o"])(lt,{label:t.$t("delete"),icon:"pi pi-trash",class:"p-button-danger mx-1",onClick:e[1]||(e[1]=function(t){return rt.notificationsToolbar("delete")})},null,8,["label"])]})),_:1}),Object(o["o"])(ft,{value:nt.notifications,filterDisplay:"menu",dataKey:"messageId",loading:nt.loading,scrollDirection:"vertical",selection:nt.selectedNotifications,"onUpdate:selection":e[2]||(e[2]=function(t){return nt.selectedNotifications=t}),class:"p-datatable-gridlines",rowHover:!0,responsiveLayout:"scroll",scrollable:!1,scrollHeight:"50vh",paginatorPosition:"top",paginator:!0,rows:20,rowsPerPageOptions:[10,20,50,100,500],pageLinkSize:5},{empty:Object(o["U"])((function(){return[Object(o["k"])("div",l,Object(o["O"])(t.$t("noNotificationFound")),1)]})),loading:Object(o["U"])((function(){return[Object(o["k"])("div",s,Object(o["O"])(t.$t("loadingNotifications")),1)]})),default:Object(o["U"])((function(){return[Object(o["o"])(dt,{selectionMode:"multiple",bodyClass:"text-center",style:{width:"5rem"}}),Object(o["o"])(dt,{field:"title",header:t.$t("title"),bodyClass:"text-center",style:{flex:"0 0 12rem"}},{body:Object(o["U"])((function(t){var e=t.data;return[Object(o["n"])(Object(o["O"])(e.title),1)]})),_:1},8,["header"]),Object(o["o"])(dt,{field:"creator",header:t.$t("creator"),bodyClass:"text-center",style:{flex:"0 0 8rem"}},{body:Object(o["U"])((function(t){var e=t.data;return[Object(o["n"])(Object(o["O"])(e.creator),1)]})),_:1},8,["header"]),Object(o["o"])(dt,{field:"visible",header:t.$t("visible"),bodyClass:"text-center",style:{flex:"0 0 5rem"}},{body:Object(o["U"])((function(t){var e=t.data;return[e.visible?(Object(o["C"])(),Object(o["j"])("i",d)):(Object(o["C"])(),Object(o["j"])("i",f))]})),_:1},8,["header"]),Object(o["o"])(dt,{field:"createDateString",header:t.$t("date"),bodyClass:"text-center",style:{flex:"0 0 7rem"}},{body:Object(o["U"])((function(t){var e=t.data;return[Object(o["n"])(Object(o["O"])(e.createDateString),1)]})),_:1},8,["header"]),Object(o["o"])(dt,{field:"createTimeString",header:t.$t("time"),bodyClass:"text-center",style:{flex:"0 0 7rem"}},{body:Object(o["U"])((function(t){var e=t.data;return[Object(o["n"])(Object(o["O"])(e.createTimeString),1)]})),_:1},8,["header"]),Object(o["o"])(dt,{bodyStyle:"display: flex;",bodyClass:"flex justify-content-evenly flex-wrap card-container text-center w-full",style:{flex:"0 0 13rem"}},{body:Object(o["U"])((function(e){var i=e.data;return[Object(o["k"])("div",u,[Object(o["V"])(Object(o["o"])(lt,{icon:"pi pi-pencil",class:"p-button-rounded p-button-warning p-button-outlined mx-1",onClick:function(t){return rt.editNotification(i.messageId)}},null,8,["onClick"]),[[Nt,t.$t("edit"),void 0,{top:!0}]])]),Object(o["k"])("div",b,[Object(o["V"])(Object(o["o"])(lt,{icon:"pi pi-trash",class:"p-button-rounded p-button-danger p-button-outlined mx-1",onClick:function(t){return rt.deleteNotification(i.messageId,i.title)}},null,8,["onClick"]),[[Nt,t.$t("delete"),void 0,{top:!0}]])])]})),_:1})]})),_:1},8,["value","loading","selection"])]})),_:1},8,["header"]),nt.createNotificationFlag?(Object(o["C"])(),Object(o["h"])(ut,{key:0,header:t.$t("createNotification")},{default:Object(o["U"])((function(){return[nt.createNotificationLoader?(Object(o["C"])(),Object(o["j"])("div",m,[Object(o["o"])(bt)])):(Object(o["C"])(),Object(o["j"])("div",p,[Object(o["k"])("div",O,[Object(o["k"])("div",j,[Object(o["k"])("div",N,[Object(o["k"])("label",g,[Object(o["n"])(Object(o["O"])(t.$t("title")),1),h]),Object(o["o"])(mt,{id:"createNotification.title",type:"text",class:Object(o["w"])(nt.createNotificationErrors.title),modelValue:nt.createNotificationBuffer.title,"onUpdate:modelValue":e[3]||(e[3]=function(t){return nt.createNotificationBuffer.title=t})},null,8,["class","modelValue"])])]),Object(o["k"])("div",v,[Object(o["k"])("div",B,[Object(o["k"])("label",y,Object(o["O"])(t.$t("visible")),1),Object(o["o"])(pt,{id:"createNotification.visible",modelValue:nt.createNotificationBuffer.visible,"onUpdate:modelValue":e[4]||(e[4]=function(t){return nt.createNotificationBuffer.visible=t})},null,8,["modelValue"])])])]),Object(o["k"])("div",x,[Object(o["k"])("div",k,[Object(o["k"])("div",T,[Object(o["k"])("label",$,Object(o["O"])(t.$t("body")),1),Object(o["o"])(Ot,{id:"createNotification.body",modelValue:nt.createNotificationBuffer.body,"onUpdate:modelValue":e[5]||(e[5]=function(t){return nt.createNotificationBuffer.body=t}),editorStyle:"height: 320px",dir:t.$store.state.reverseDirection},null,8,["modelValue","dir"])])])]),Object(o["o"])(lt,{label:t.$t("confirm"),class:"p-button-success mt-3 mx-1",onClick:e[6]||(e[6]=function(t){return rt.createNotificationCheckup()})},null,8,["label"]),Object(o["o"])(lt,{label:t.$t("back"),class:"p-button-danger mt-3 mx-1",onClick:e[7]||(e[7]=function(t){return rt.resetState("createNotification")})},null,8,["label"])]))]})),_:1},8,["header"])):Object(o["i"])("",!0),nt.editNotificationFlag?(Object(o["C"])(),Object(o["h"])(ut,{key:1,header:t.$t("editNotification")},{default:Object(o["U"])((function(){return[nt.editNotificationLoader?(Object(o["C"])(),Object(o["j"])("div",F,[Object(o["o"])(bt)])):(Object(o["C"])(),Object(o["j"])("div",D,[Object(o["k"])("div",S,[Object(o["k"])("div",C,[Object(o["k"])("div",M,[Object(o["k"])("label",E,Object(o["O"])(t.$t("id")),1),Object(o["o"])(mt,{id:"editNotification.messageId",type:"text",modelValue:nt.editNotificationBuffer.messageId,"onUpdate:modelValue":e[8]||(e[8]=function(t){return nt.editNotificationBuffer.messageId=t}),disabled:""},null,8,["modelValue"])])])]),Object(o["k"])("div",V,[Object(o["k"])("div",I,[Object(o["k"])("div",A,[Object(o["k"])("label",P,Object(o["O"])(t.$t("creatorId")),1),Object(o["o"])(mt,{id:"editNotification.creator",type:"text",modelValue:nt.editNotificationBuffer.creator,"onUpdate:modelValue":e[9]||(e[9]=function(t){return nt.editNotificationBuffer.creator=t}),disabled:""},null,8,["modelValue"])])]),Object(o["k"])("div",U,[Object(o["k"])("div",q,[Object(o["k"])("label",L,Object(o["O"])(t.$t("editorId")),1),Object(o["o"])(mt,{id:"editNotification.updater",type:"text",modelValue:nt.editNotificationBuffer.updater,"onUpdate:modelValue":e[10]||(e[10]=function(t){return nt.editNotificationBuffer.updater=t}),disabled:""},null,8,["modelValue"])])])]),Object(o["k"])("div",w,[Object(o["k"])("div",R,[Object(o["k"])("div",_,[Object(o["k"])("label",K,Object(o["O"])(t.$t("createDateTime")),1),Object(o["o"])(mt,{id:"editNotification.createDateTime",type:"text",modelValue:nt.editNotificationBuffer.createDateTimeString,"onUpdate:modelValue":e[11]||(e[11]=function(t){return nt.editNotificationBuffer.createDateTimeString=t}),disabled:""},null,8,["modelValue"])])]),Object(o["k"])("div",J,[Object(o["k"])("div",G,[Object(o["k"])("label",H,Object(o["O"])(t.$t("editDateTime")),1),Object(o["o"])(mt,{id:"editNotification.editDateTime",type:"text",modelValue:nt.editNotificationBuffer.updateDateTimeString,"onUpdate:modelValue":e[12]||(e[12]=function(t){return nt.editNotificationBuffer.updateDateTimeString=t}),disabled:""},null,8,["modelValue"])])])]),Object(o["k"])("div",z,[Object(o["k"])("div",Q,[Object(o["k"])("div",W,[Object(o["k"])("label",X,[Object(o["n"])(Object(o["O"])(t.$t("title")),1),Y]),Object(o["o"])(mt,{id:"editNotification.title",type:"text",class:Object(o["w"])(nt.editNotificationErrors.title),modelValue:nt.editNotificationBuffer.title,"onUpdate:modelValue":e[13]||(e[13]=function(t){return nt.editNotificationBuffer.title=t})},null,8,["class","modelValue"])])]),Object(o["k"])("div",Z,[Object(o["k"])("div",tt,[Object(o["k"])("label",et,Object(o["O"])(t.$t("visible")),1),Object(o["o"])(pt,{id:"editNotification.visible",modelValue:nt.editNotificationBuffer.visible,"onUpdate:modelValue":e[14]||(e[14]=function(t){return nt.editNotificationBuffer.visible=t})},null,8,["modelValue"])])])]),Object(o["k"])("div",it,[Object(o["k"])("div",ot,[Object(o["k"])("div",at,[Object(o["k"])("label",ct,Object(o["O"])(t.$t("body")),1),Object(o["o"])(Ot,{id:"editNotification.body",modelValue:nt.editNotificationBuffer.body,"onUpdate:modelValue":e[15]||(e[15]=function(t){return nt.editNotificationBuffer.body=t}),editorStyle:"height: 320px",dir:t.$store.state.reverseDirection},null,8,["modelValue","dir"])])])]),Object(o["o"])(lt,{label:t.$t("confirm"),class:"p-button-success mt-3 mx-1",onClick:e[16]||(e[16]=function(t){return rt.editNotificationCheckup()})},null,8,["label"]),Object(o["o"])(lt,{label:t.$t("back"),class:"p-button-danger mt-3 mx-1",onClick:e[17]||(e[17]=function(t){return rt.resetState("editNotification")})},null,8,["label"])]))]})),_:1},8,["header"])):Object(o["i"])("",!0)]})),_:1},8,["activeIndex"])])])])}var rt=i("e6da"),lt=i.n(rt),st={name:"Notifications",data:function(){return{notifications:[],selectedNotifications:[],createNotificationBuffer:{title:"",visible:!1,body:""},editNotificationBuffer:{messageId:"",creator:"",title:"",visible:!1,body:""},createNotificationErrors:{title:""},editNotificationErrors:{title:""},tabActiveIndex:0,notificationToolbarBuffer:"",loading:!0,createNotificationFlag:!1,editNotificationFlag:!1,createNotificationLoader:!1,editNotificationLoader:!1}},mounted:function(){this.notificationsRequestMaster("getNotifications")},methods:{notificationsRequestMaster:function(t){var e=this,i="";if("Fa"===this.$i18n.locale?i="fa":"En"===this.$i18n.locale&&(i="en"),"getNotifications"===t)this.loading=!0,this.axios({url:"/api/users/publicMessages",method:"GET",params:{lang:i}}).then((function(t){if(200===t.data.status.code){for(var i in e.notifications=t.data.data,e.notifications)e.notifications[i].createDateString=e.notifications[i].createTime.year+"/"+e.notifications[i].createTime.month+"/"+e.notifications[i].createTime.day,e.notifications[i].createTimeString=e.notifications[i].createTime.hours+":"+e.notifications[i].createTime.minutes+":"+e.notifications[i].createTime.seconds;e.loading=!1}else e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}));else if("getNotification"===t)this.editNotificationLoader=!0,this.axios({url:"/api/users/publicMessages",method:"GET",params:{id:e.editNotificationBuffer.messageId,lang:i}}).then((function(t){200===t.data.status.code?(e.editNotificationBuffer=t.data.data[0],e.editNotificationBuffer.createDateString=e.editNotificationBuffer.createTime.year+"/"+e.editNotificationBuffer.createTime.month+"/"+e.editNotificationBuffer.createTime.day,e.editNotificationBuffer.createTimeString=e.editNotificationBuffer.createTime.hours+":"+e.editNotificationBuffer.createTime.minutes+":"+e.editNotificationBuffer.createTime.seconds,e.editNotificationBuffer.createDateTimeString=e.editNotificationBuffer.createDateString+" - "+e.editNotificationBuffer.createTimeString,"undefined"!==typeof e.editNotificationBuffer.updateTime&&(e.editNotificationBuffer.updateDateString=e.editNotificationBuffer.updateTime.year+"/"+e.editNotificationBuffer.updateTime.month+"/"+e.editNotificationBuffer.updateTime.day,e.editNotificationBuffer.updateTimeString=e.editNotificationBuffer.updateTime.hours+":"+e.editNotificationBuffer.updateTime.minutes+":"+e.editNotificationBuffer.updateTime.seconds,e.editNotificationBuffer.updateDateTimeString=e.editNotificationBuffer.updateDateString+" - "+e.editNotificationBuffer.updateTimeString),e.editNotificationLoader=!1):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.editNotificationLoader=!1,e.resetState("editNotification"))})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.editNotificationLoader=!1,e.resetState("editNotification")}));else if("createNotification"===t)this.createNotificationLoader=!0,this.axios({url:"/api/users/publicMessage",method:"POST",headers:{"Content-Type":"application/json"},params:{lang:i},data:JSON.stringify({title:e.createNotificationBuffer.title,visible:e.createNotificationBuffer.visible,body:e.createNotificationBuffer.body}).replace(/\\\\/g,"\\")}).then((function(t){200===t.data.status.code?(e.createNotificationLoader=!1,e.resetState("createNotification"),e.notificationsRequestMaster("getNotifications")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.createNotificationLoader=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.createNotificationLoader=!1}));else if("editNotification"===t)this.editNotificationLoader=!0,this.axios({url:"/api/users/publicMessage",method:"PUT",headers:{"Content-Type":"application/json"},params:{lang:i},data:JSON.stringify({title:e.editNotificationBuffer.title,visible:e.editNotificationBuffer.visible,body:e.editNotificationBuffer.body}).replace(/\\\\/g,"\\")}).then((function(t){200===t.data.status.code?(e.editNotificationLoader=!1,e.resetState("editNotification"),e.notificationsRequestMaster("getNotifications")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.editNotificationLoader=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.editNotificationLoader=!1}));else if("deleteNotification"===t){var o=[this.notificationToolbarBuffer];this.loading=!0,this.axios({url:"/api/users/publicMessages",method:"DELETE",headers:{"Content-Type":"application/json"},params:{lang:i},data:JSON.stringify({names:o}).replace(/\\\\/g,"\\")}).then((function(t){200===t.data.status.code?(e.loading=!1,e.notificationsRequestMaster("getNotifications")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))}else if("deleteNotifications"===t){var a=[];for(var c in this.selectedNotifications)a.push(this.selectedNotifications[c].messageId);this.loading=!0,this.axios({url:"/api/users/publicMessages",method:"DELETE",headers:{"Content-Type":"application/json"},params:{lang:i},data:JSON.stringify({names:a}).replace(/\\\\/g,"\\")}).then((function(t){200===t.data.status.code?(e.loading=!1,e.notificationsRequestMaster("getNotifications")):(e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1)})).catch((function(){e.alertPromptMaster(e.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),e.loading=!1}))}},alertPromptMaster:function(t,e,i,o){var a=!0;"ltr"===this.$store.state.direction&&(a=!1),lt.a.show({title:t,message:e,position:"center",icon:"pi "+i,backgroundColor:o,transitionIn:"fadeInLeft",rtl:a,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button my-3' style='border-radius: 6px;'>"+this.$t("confirm")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e)}]]})},confirmPromptMaster:function(t,e,i,o,a,c){var n=!0;"ltr"===this.$store.state.direction&&(n=!1),lt.a.show({title:t,message:e,position:"center",icon:"pi "+i,backgroundColor:o,transitionIn:"fadeInLeft",rtl:n,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>"+this.$t("yes")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e),a(c)}],["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>"+this.$t("no")+"</button>",function(t,e){t.hide({transitionOut:"fadeOutRight"},e)}]]})},createNotification:function(){this.createNotificationFlag=!0,this.tabActiveIndex=1},createNotificationCheckup:function(){var t=0;""===this.createNotificationBuffer.title?(this.createNotificationErrors.title="p-invalid",t+=1):this.createNotificationErrors.title="",t>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.notificationsRequestMaster("createNotification")},editNotification:function(t){this.editNotificationBuffer.messageId=t,this.editNotificationFlag=!0,this.createNotificationFlag?this.tabActiveIndex=2:this.tabActiveIndex=1,this.notificationsRequestMaster("getNotification")},editNotificationCheckup:function(){var t=0;""===this.editNotificationBuffer.title?(this.editNotificationErrors.title="p-invalid",t+=1):this.editNotificationErrors.title="",t>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("editNotification"),"pi-question-circle","#F0EAAA",this.notificationsRequestMaster,"editNotification")},deleteNotification:function(t,e){this.notificationToolbarBuffer=t,this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("delete")+" "+String(e),"pi-question-circle","#F0EAAA",this.notificationsRequestMaster,"deleteNotification")},notificationsToolbar:function(t){this.selectedNotifications.length>0?"delete"===t&&this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("delete")+" "+this.$t("notifications"),"pi-question-circle","#F0EAAA",this.notificationsRequestMaster,"deleteNotifications"):this.alertPromptMaster(this.$t("noNotificationSelected"),"","pi-exclamation-triangle","#FDB5BA")},resetState:function(t){"createNotification"===t?(this.tabActiveIndex=0,this.createNotificationFlag=!1,this.createNotificationBuffer={title:"",visible:!1,body:""}):"editNotification"===t&&(this.tabActiveIndex=0,this.editNotificationFlag=!1,this.editNotificationBuffer={title:"",visible:!1,body:""})}}},dt=(i("7a42"),i("6b0d")),ft=i.n(dt);const ut=ft()(st,[["render",nt],["__scopeId","data-v-4749e7fe"]]);e["default"]=ut}}]);
//# sourceMappingURL=chunk-7939ddb1.125f3c41.js.map