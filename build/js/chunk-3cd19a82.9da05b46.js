(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3cd19a82"],{"5c52":function(e,t,a){"use strict";a("5d38")},"5d38":function(e,t,a){},bb18:function(e,t,a){var r,s,i;(function(){var a={},o="[۰-۹]",l=["[کگۀی،","تثجحخد","غيًٌٍَ","ُپٰچژ‌","ء-ةذ-عف-ٔ]"].join(""),n="(،|؟|«|»|؛|٬)",c="(\\.|:|\\!|\\-|\\[|\\]|\\(|\\)|/)";function u(){for(var e="(",t=0;t<arguments.length;t++)e+="(",e+=t!=arguments.length-1?arguments[t]+")|":arguments[t]+")";return e+")"}a.number=new RegExp("^"+o+"+$"),a.letter=new RegExp("^"+l+"+$"),a.punctuation=new RegExp("^"+u(n,c)+"+$"),a.text=new RegExp("^"+u(o,l,n,c,"\\s")+"+$"),a.rtl=new RegExp("^"+u(l,o,n,"\\s")+"+$"),a.hasNumber=new RegExp(o),a.hasLetter=new RegExp(l),a.hasPunctuation=new RegExp(u(n,c)),a.hasText=new RegExp(u(o,l,n,c)),a.hasRtl=new RegExp(u(o,l,n)),a.numbersASCIRange=o,a.lettersASCIRange=l,a.rtlPunctuationsASCIRange=n,a.ltrPunctuationsASCIRange=c,s=[],r=a,i="function"===typeof r?r.apply(t,s):r,void 0===i||(e.exports=i)})()},c66d:function(e,t,a){"use strict";a.r(t);var r=a("7a23"),s=function(e){return Object(r["F"])("data-v-28a75f40"),e=e(),Object(r["D"])(),e},i={class:"grid"},o={class:"col-12"},l={class:"card"},n={key:0,class:"text-center"},c={key:1},u={class:"formgrid grid"},d={class:"field col-3"},p={class:"flex align-content-center flex-wrap h-full"},b=["href"],f={class:"hidden"},m={class:"grid"},h={class:"col-12"},O={class:"col-12"},j=s((function(){return Object(r["k"])("div",{class:"field col-9"},null,-1)})),g={class:"formgrid grid"},v={class:"field col-6"},k={class:"field p-fluid"},y={for:"user._id"},w={class:"formgrid grid"},x={class:"field col"},C={class:"field p-fluid"},E={for:"user.displayName"},P=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),A={class:"field col"},$={class:"field p-fluid"},N={for:"user.employeeNumber"},R={class:"formgrid grid"},I={class:"field col"},F={class:"field p-fluid"},q={for:"user.firstName"},B=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),U={class:"field col"},V={class:"field p-fluid"},D={for:"user.lastName"},M=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),T={class:"formgrid grid"},_={class:"field col"},Q={class:"field p-fluid"},K={for:"user.mobile"},S=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),L={class:"field col"},z={class:"field p-fluid"},G={for:"user.mail"},J=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),Z={class:"formgrid grid"},H={class:"field col"},W={class:"field p-fluid"},X={for:"user.description"},Y={key:0,class:"text-center"},ee={key:1},te={class:"formgrid grid"},ae={class:"field col"},re={class:"field p-fluid"},se={for:"user.password"},ie=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),oe={class:"mt-3"},le={class:"pl-2 ml-2 mt-0",style:{"line-height":"1.5"}},ne={key:0},ce={key:1},ue={key:2},de={key:3},pe={class:"field col"},be={class:"field p-fluid"},fe={for:"user.passwordRepeat"},me=s((function(){return Object(r["k"])("span",{style:{color:"red"}}," * ",-1)})),he={class:"formgrid grid"},Oe={class:"field col-6"},je={class:"field p-fluid"},ge={class:"p-inputgroup"},ve=s((function(){return Object(r["k"])("span",{id:"countdownButtonCD"},null,-1)}));function ke(e,t,a,s,ke,ye){var we=Object(r["K"])("ProgressSpinner"),xe=Object(r["K"])("AppAvatar"),Ce=Object(r["K"])("Button"),Ee=Object(r["K"])("InputText"),Pe=Object(r["K"])("Textarea"),Ae=Object(r["K"])("TabPanel"),$e=Object(r["K"])("Divider"),Ne=Object(r["K"])("Password"),Re=Object(r["K"])("TabView"),Ie=Object(r["L"])("tooltip");return Object(r["C"])(),Object(r["j"])("div",i,[Object(r["k"])("div",o,[Object(r["k"])("div",l,[Object(r["k"])("h3",null,Object(r["O"])(e.$t("profile")),1),Object(r["o"])(Re,{activeIndex:ke.tabActiveIndex,"onUpdate:activeIndex":t[25]||(t[25]=function(e){return ke.tabActiveIndex=e})},{default:Object(r["U"])((function(){return[Object(r["o"])(Ae,{header:e.$t("editUserInformation")},{default:Object(r["U"])((function(){return[ke.loading?(Object(r["C"])(),Object(r["j"])("div",n,[Object(r["o"])(we)])):(Object(r["C"])(),Object(r["j"])("div",c,[Object(r["k"])("div",u,[Object(r["k"])("div",d,[Object(r["k"])("div",p,[Object(r["k"])("a",{href:ke.userAvatar,target:"_blank",class:"flex align-items-center justify-content-center avatar"},[Object(r["o"])(xe,{image:ke.userAvatar,size:"xlarge",shape:"circle",class:"avatar avatarBorder"},null,8,["image"])],8,b),Object(r["k"])("form",f,[Object(r["k"])("input",{id:"user.avatar",type:"file",name:"file",onChange:t[0]||(t[0]=function(e){return ye.profileRequestMaster("editUserAvatar")}),accept:".png, .jpg, .jpeg"},null,32)]),Object(r["k"])("div",m,[Object(r["k"])("div",h,[Object(r["V"])(Object(r["o"])(Ce,{icon:"pi pi-pencil",class:"p-button-rounded mr-2 mb-2",onClick:t[1]||(t[1]=function(e){return ye.editAvatarHelper()})},null,512),[[Ie,e.$t("editAvatar"),void 0,{top:!0}]])]),Object(r["k"])("div",O,[Object(r["V"])(Object(r["o"])(Ce,{icon:"pi pi-trash",class:"p-button-danger p-button-rounded mr-2 mb-2",onClick:t[2]||(t[2]=function(e){return ye.deleteUserAvatarCheckup()})},null,512),[[Ie,e.$t("deleteAvatar"),void 0,{top:!0}]])])])])]),j]),Object(r["k"])("div",g,[Object(r["k"])("div",v,[Object(r["k"])("div",k,[Object(r["k"])("label",y,Object(r["O"])(e.$t("id")),1),Object(r["o"])(Ee,{id:"user._id",type:"text",class:Object(r["w"])(ke.userErrors._id),modelValue:ke.user._id,"onUpdate:modelValue":t[3]||(t[3]=function(e){return ke.user._id=e}),onKeypress:t[4]||(t[4]=function(e){return ye.englishInputFilter(e)}),onPaste:t[5]||(t[5]=function(e){return ye.englishInputFilter(e)}),disabled:!0},null,8,["class","modelValue"])])])]),Object(r["k"])("div",w,[Object(r["k"])("div",x,[Object(r["k"])("div",C,[Object(r["k"])("label",E,[Object(r["n"])(Object(r["O"])(e.$t("persianName")),1),P]),Object(r["o"])(Ee,{id:"user.displayName",type:"text",class:Object(r["w"])(ke.userErrors.displayName),modelValue:ke.user.displayName,"onUpdate:modelValue":t[6]||(t[6]=function(e){return ke.user.displayName=e}),onKeypress:t[7]||(t[7]=function(e){return ye.persianInputFilter(e)}),onPaste:t[8]||(t[8]=function(e){return ye.persianInputFilter(e)}),disabled:ke.user.profileInaccessibility},null,8,["class","modelValue","disabled"]),Object(r["k"])("small",null,Object(r["O"])(e.$t("inputPersianFilterText")),1)])]),Object(r["k"])("div",A,[Object(r["k"])("div",$,[Object(r["k"])("label",N,Object(r["O"])(e.$t("employeeNumber")),1),Object(r["o"])(Ee,{id:"user.employeeNumber",type:"text",modelValue:ke.user.employeeNumber,"onUpdate:modelValue":t[9]||(t[9]=function(e){return ke.user.employeeNumber=e}),disabled:ke.user.profileInaccessibility},null,8,["modelValue","disabled"])])])]),Object(r["k"])("div",R,[Object(r["k"])("div",I,[Object(r["k"])("div",F,[Object(r["k"])("label",q,[Object(r["n"])(Object(r["O"])(e.$t("englishFirstName")),1),B]),Object(r["o"])(Ee,{id:"user.firstName",type:"text",class:Object(r["w"])(ke.userErrors.firstName),modelValue:ke.user.firstName,"onUpdate:modelValue":t[10]||(t[10]=function(e){return ke.user.firstName=e}),onKeypress:t[11]||(t[11]=function(e){return ye.englishInputFilter(e)}),onPaste:t[12]||(t[12]=function(e){return ye.englishInputFilter(e)}),disabled:ke.user.profileInaccessibility},null,8,["class","modelValue","disabled"]),Object(r["k"])("small",null,Object(r["O"])(e.$t("inputEnglishFilterText")),1)])]),Object(r["k"])("div",U,[Object(r["k"])("div",V,[Object(r["k"])("label",D,[Object(r["n"])(Object(r["O"])(e.$t("englishLastName")),1),M]),Object(r["o"])(Ee,{id:"user.lastName",type:"text",class:Object(r["w"])(ke.userErrors.lastName),modelValue:ke.user.lastName,"onUpdate:modelValue":t[13]||(t[13]=function(e){return ke.user.lastName=e}),onKeypress:t[14]||(t[14]=function(e){return ye.englishInputFilter(e)}),onPaste:t[15]||(t[15]=function(e){return ye.englishInputFilter(e)}),disabled:ke.user.profileInaccessibility},null,8,["class","modelValue","disabled"]),Object(r["k"])("small",null,Object(r["O"])(e.$t("inputEnglishFilterText")),1)])])]),Object(r["k"])("div",T,[Object(r["k"])("div",_,[Object(r["k"])("div",Q,[Object(r["k"])("label",K,[Object(r["n"])(Object(r["O"])(e.$t("mobile")),1),S]),Object(r["o"])(Ee,{id:"user.mobile",type:"text",class:Object(r["w"])(ke.userErrors.mobile),modelValue:ke.user.mobile,"onUpdate:modelValue":t[16]||(t[16]=function(e){return ke.user.mobile=e}),disabled:ke.user.profileInaccessibility},null,8,["class","modelValue","disabled"])])]),Object(r["k"])("div",L,[Object(r["k"])("div",z,[Object(r["k"])("label",G,[Object(r["n"])(Object(r["O"])(e.$t("email")),1),J]),Object(r["o"])(Ee,{id:"user.mail",type:"text",class:Object(r["w"])(ke.userErrors.mail),modelValue:ke.user.mail,"onUpdate:modelValue":t[17]||(t[17]=function(e){return ke.user.mail=e}),disabled:ke.user.profileInaccessibility},null,8,["class","modelValue","disabled"])])])]),Object(r["k"])("div",Z,[Object(r["k"])("div",H,[Object(r["k"])("div",W,[Object(r["k"])("label",X,Object(r["O"])(e.$t("description")),1),Object(r["o"])(Pe,{id:"user.description",modelValue:ke.user.description,"onUpdate:modelValue":t[18]||(t[18]=function(e){return ke.user.description=e}),autoResize:!0,rows:"3",disabled:ke.user.profileInaccessibility},null,8,["modelValue","disabled"])])])]),Object(r["o"])(Ce,{id:"user.editButton",label:e.$t("confirm"),class:"p-button-success mt-3 mx-1",onClick:t[19]||(t[19]=function(e){return ye.editUserInformationCheckup()}),style:Object(r["x"])(ke.user.profileInaccessibility?"display: none;":"")},null,8,["label","style"])]))]})),_:1},8,["header"]),Object(r["o"])(Ae,{header:e.$t("editPassword")},{default:Object(r["U"])((function(){return[ke.loading?(Object(r["C"])(),Object(r["j"])("div",Y,[Object(r["o"])(we)])):(Object(r["C"])(),Object(r["j"])("div",ee,[Object(r["k"])("div",te,[Object(r["k"])("div",ae,[Object(r["k"])("div",re,[Object(r["k"])("label",se,[Object(r["n"])(Object(r["O"])(e.$t("password")),1),ie]),Object(r["o"])(Ne,{id:"user.password",class:Object(r["w"])(ke.userErrors.userPassword),modelValue:ke.user.userPassword,"onUpdate:modelValue":t[20]||(t[20]=function(e){return ke.user.userPassword=e}),toggleMask:!0,autocomplete:"off"},{footer:Object(r["U"])((function(){return[Object(r["o"])($e),Object(r["k"])("p",oe,Object(r["O"])(e.$t("passwordRequirement")),1),Object(r["k"])("ul",le,[ke.passwordQualityCheck.smallalphabet?(Object(r["C"])(),Object(r["j"])("li",ne,Object(r["O"])(e.$t("passwordRequirementText1")),1)):Object(r["i"])("",!0),ke.passwordQualityCheck.capitalalphabet?(Object(r["C"])(),Object(r["j"])("li",ce,Object(r["O"])(e.$t("passwordRequirementText2")),1)):Object(r["i"])("",!0),ke.passwordQualityCheck.number?(Object(r["C"])(),Object(r["j"])("li",ue,Object(r["O"])(e.$t("passwordRequirementText3")),1)):Object(r["i"])("",!0),ke.passwordQualityCheck.specialchar?(Object(r["C"])(),Object(r["j"])("li",de,Object(r["O"])(e.$t("passwordRequirementText4")),1)):Object(r["i"])("",!0),Object(r["k"])("li",null,Object(r["O"])(e.$t("passwordRequirementText5")+ke.passwordQualityCheck.length+e.$t("passwordRequirementText6")),1)])]})),_:1},8,["class","modelValue"])])]),Object(r["k"])("div",pe,[Object(r["k"])("div",be,[Object(r["k"])("label",fe,[Object(r["n"])(Object(r["O"])(e.$t("passwordRepeat")),1),me]),Object(r["o"])(Ne,{id:"user.passwordRepeat",class:Object(r["w"])(ke.userErrors.userPasswordRepeat),modelValue:ke.user.userPasswordRepeat,"onUpdate:modelValue":t[21]||(t[21]=function(e){return ke.user.userPasswordRepeat=e}),toggleMask:!0,onpaste:"return false;",ondrop:"return false;",autocomplete:"off"},null,8,["class","modelValue"])])])]),Object(r["k"])("div",he,[Object(r["k"])("div",Oe,[Object(r["k"])("div",je,[Object(r["k"])("div",ge,[Object(r["V"])(Object(r["o"])(Ee,{modelValue:ke.user.verificationCode,"onUpdate:modelValue":t[22]||(t[22]=function(e){return ke.user.verificationCode=e}),placeholder:e.$t("verificationCode")},null,8,["modelValue","placeholder"]),[[Ie,e.$t("profileText1"),void 0,{top:!0}]]),Object(r["o"])(Ce,{id:"countdownButton",onClick:t[23]||(t[23]=function(e){return ye.profileRequestMaster("requestVerificationCode")})},{default:Object(r["U"])((function(){return[Object(r["n"])(Object(r["O"])(e.$t("request"))+" ",1),ve]})),_:1})])])])]),Object(r["o"])(Ce,{label:e.$t("confirm"),class:"p-button-success mx-1",onClick:t[24]||(t[24]=function(e){return ye.editUserPasswordCheckup()})},null,8,["label"])]))]})),_:1},8,["header"])]})),_:1},8,["activeIndex"])])])])}var ye=a("e6da"),we=a.n(ye),xe={name:"Profile",data:function(){return{user:{_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",employeeNumber:"",description:"",userPassword:"",userPasswordRepeat:"",verificationCode:"",profileInaccessibility:!1},userErrors:{_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",userPassword:"",userPasswordRepeat:"",verificationCode:""},passwordQualityCheck:{smallalphabet:!1,capitalalphabet:!1,number:!1,specialchar:!1,length:"8",regex:"^"},userAvatar:"images/avatarPlaceholder.png",verificationCodeCountdown:"",tabActiveIndex:0,loading:!1,persianRex:null}},mounted:function(){this.persianRex=a("bb18"),this.profileRequestMaster("getUserAvatar"),this.profileRequestMaster("getUser"),this.profileRequestMaster("getPasswordQuality")},methods:{profileRequestMaster:function(e){var t=this,a="";if("Fa"===this.$i18n.locale?a="fa":"En"===this.$i18n.locale&&(a="en"),"getUser"===e)this.user={_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",employeeNumber:"",description:"",userPassword:"",userPasswordRepeat:"",verificationCode:"",profileInaccessibility:!1},this.loading=!0,this.axios({url:"/api/user",method:"GET",params:{lang:a}}).then((function(e){200===e.data.status.code?(t.loading=!1,t.user._id=e.data.data._id,t.user.displayName=e.data.data.displayName,t.user.firstName=e.data.data.firstName,t.user.lastName=e.data.data.lastName,t.user.mobile=e.data.data.mobile,t.user.mail=e.data.data.mail,t.user.employeeNumber=e.data.data.employeeNumber,t.user.description=e.data.data.description,t.user.profileInaccessibility=e.data.data.profileInaccessibility):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}));else if("editUserInformation"===e)this.loading=!0,this.axios({url:"/api/user",method:"PUT",headers:{"Content-Type":"application/json"},params:{lang:a},data:JSON.stringify({_id:t.user._id,displayName:t.user.displayName,firstName:t.user.firstName,lastName:t.user.lastName,mobile:t.user.mobile,mail:t.user.mail,employeeNumber:t.user.employeeNumber,description:t.user.description}).replace(/\\\\/g,"\\")}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUser")):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}));else if("editUserPassword"===e)this.loading=!0,this.axios({url:"/api/user/password",method:"PUT",headers:{"Content-Type":"application/json"},params:{lang:a},data:JSON.stringify({newPassword:t.user.userPassword,token:t.user.verificationCode}).replace(/\\\\/g,"\\")}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUser")):302===e.data.status.code||403===e.data.status.code||429===e.data.status.code?(t.alertPromptMaster(e.data.status.result,"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}));else if("requestVerificationCode"===e)this.axios({url:"/api/user/password/request",method:"GET",params:{lang:a}}).then((function(e){200===e.data.status.code?(t.setCountdown(60*e.data.data),document.getElementById("countdownButton").disabled=!0):t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA")})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA")}));else if("getUserAvatar"===e)this.axios({url:"/api/user/photo",method:"GET"}).then((function(e){"Problem"!==e.data&&"NotExist"!==e.data&&(t.userAvatar="/api/user/photo")}));else if("editUserAvatar"===e){var r=new FormData;r.append("file",document.getElementById("user.avatar").files[0]),this.loading=!0,this.axios({url:"/api/user/photo",method:"POST",headers:{"Content-Type":"application/json"},data:r}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUserAvatar")):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))}else"deleteUserAvatar"===e?(this.loading=!0,this.axios({url:"/api/user/photo",method:"DELETE"}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUserAvatar")):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))):"getPasswordQuality"===e&&(this.loading=!0,this.axios({url:"/api/public/properties/settings",method:"GET"}).then((function(e){if(200===e.data.status.code){for(var a in e.data.data)"password.quality.capitalalphabet"===e.data.data[a]._id&&"true"===e.data.data[a].value?(t.passwordQualityCheck.capitalalphabet=!0,t.passwordQualityCheck.regex+="(?=.*[A-Z])"):"password.quality.smallalphabet"===e.data.data[a]._id&&"true"===e.data.data[a].value?(t.passwordQualityCheck.smallalphabet=!0,t.passwordQualityCheck.regex+="(?=.*[a-z])"):"password.quality.number"===e.data.data[a]._id&&"true"===e.data.data[a].value?(t.passwordQualityCheck.number=!0,t.passwordQualityCheck.regex+="(?=.*[0-9])"):"password.quality.specialchar"===e.data.data[a]._id&&"true"===e.data.data[a].value?(t.passwordQualityCheck.specialchar=!0,t.passwordQualityCheck.regex+="(?=.*[!@#$%^&*+=])"):"password.quality.length"===e.data.data[a]._id&&(t.passwordQualityCheck.length=e.data.data[a].value,t.passwordQualityCheck.regex+="(?=.{"+e.data.data[a].value+",})");t.passwordQualityCheck.regex+=".*$"}t.loading=!1})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1})))},alertPromptMaster:function(e,t,a,r){var s=!0;"ltr"===this.$store.state.direction&&(s=!1),we.a.show({title:e,message:t,position:"center",icon:"pi "+a,backgroundColor:r,transitionIn:"fadeInLeft",rtl:s,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button my-3' style='border-radius: 6px;'>"+this.$t("confirm")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t)}]]})},confirmPromptMaster:function(e,t,a,r,s,i){var o=!0;"ltr"===this.$store.state.direction&&(o=!1),we.a.show({title:e,message:t,position:"center",icon:"pi "+a,backgroundColor:r,transitionIn:"fadeInLeft",rtl:o,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>"+this.$t("yes")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t),s(i)}],["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>"+this.$t("no")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t)}]]})},editUserInformationCheckup:function(){var e=0,t=/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,a=/^(\+98|0)?9\d{9}$/;""===this.user._id?(this.userErrors._id="p-invalid",e+=1):this.userErrors._id="",""===this.user.displayName?(this.userErrors.displayName="p-invalid",e+=1):this.userErrors.displayName="",""===this.user.firstName?(this.userErrors.firstName="p-invalid",e+=1):this.userErrors.firstName="",""===this.user.lastName?(this.userErrors.lastName="p-invalid",e+=1):this.userErrors.lastName="",""===this.user.mobile?(this.userErrors.mobile="p-invalid",e+=1):a.test(Number(this.user.mobile))?this.userErrors.mobile="":(this.userErrors.mobile="p-invalid",e+=1),""===this.user.mail?(this.userErrors.mail="p-invalid",e+=1):t.test(this.user.mail)?this.userErrors.mail="":(this.userErrors.mail="p-invalid",e+=1),e>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("editUserInformation"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"editUserInformation")},editUserPasswordCheckup:function(){var e=0,t=new RegExp(this.passwordQualityCheck.regex);""===this.user.verificationCode?(this.userErrors.verificationCode="p-invalid",e+=1):this.userErrors.verificationCode="",""===this.user.userPassword?(this.userErrors.userPassword="p-invalid",e+=1):""===this.user.userPasswordRepeat?(this.userErrors.userPasswordRepeat="p-invalid",e+=1):this.user.userPassword!==this.user.userPasswordRepeat?(this.userErrors.userPassword="p-invalid",this.userErrors.userPasswordRepeat="p-invalid",e+=1):t.test(this.user.userPassword)?(this.userErrors.userPassword="",this.userErrors.userPasswordRepeat=""):(this.userErrors.userPassword="p-invalid",e+=1),e>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("editPassword"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"editUserPassword")},deleteUserAvatarCheckup:function(){this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("deleteAvatar"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"deleteUserAvatar")},editAvatarHelper:function(){document.getElementById("user.avatar").click()},setCountdown:function(e){var t,a,r=e,s=setInterval((function(){t=parseInt(r/60,10),a=parseInt(r%60,10),t=t<10?"0"+t:t,a=a<10?"0"+a:a,"undefined"!==typeof document.querySelector("#countdownButtonCD")&&(document.querySelector("#countdownButtonCD").textContent=" ("+t+":"+a+") "),--r<0&&(clearInterval(s),document.querySelector("#countdownButtonCD").textContent="",document.getElementById("countdownButton").disabled=!1)}),1e3)},englishInputFilter:function(e){if("keypress"===e.type){var t=e.keyCode?e.keyCode:e.which;(t<48||t>122||t>57&&t<65||t>90&&t<97)&&e.preventDefault()}else if("paste"===e.type)for(var a=e.clipboardData.getData("text"),r=0;r<a.length;++r){if(a[r].charCodeAt(0)<48||a[r].charCodeAt(0)>122){e.preventDefault();break}if(a[r].charCodeAt(0)>57&&a[r].charCodeAt(0)<65){e.preventDefault();break}if(a[r].charCodeAt(0)>90&&a[r].charCodeAt(0)<97){e.preventDefault();break}}},persianInputFilter:function(e){if("keypress"===e.type){var t=e.key?e.key:e.which,a=e.keyCode?e.keyCode:e.which;(a<48||a>57)&&(a>32&&a<65||a>90&&a<97||a>122&&a<127?e.preventDefault():this.persianRex.text.test(t)||e.preventDefault())}else if("paste"===e.type)for(var r=e.clipboardData.getData("text"),s=0;s<r.length;++s)if(r[s].charCodeAt(0)<48||r[s].charCodeAt(0)>57){if(r[s].charCodeAt(0)>32&&r[s].charCodeAt(0)<65){e.preventDefault();break}if(r[s].charCodeAt(0)>90&&r[s].charCodeAt(0)<97){e.preventDefault();break}if(r[s].charCodeAt(0)>122&&r[s].charCodeAt(0)<127){e.preventDefault();break}if(!this.persianRex.text.test(r[s])){e.preventDefault();break}}}}},Ce=(a("5c52"),a("6b0d")),Ee=a.n(Ce);const Pe=Ee()(xe,[["render",ke],["__scopeId","data-v-28a75f40"]]);t["default"]=Pe}}]);
//# sourceMappingURL=chunk-3cd19a82.9da05b46.js.map