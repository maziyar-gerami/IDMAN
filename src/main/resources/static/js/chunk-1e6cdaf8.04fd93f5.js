(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1e6cdaf8"],{"914c":function(e,t,r){"use strict";r("b66a")},b66a:function(e,t,r){},bb18:function(e,t,r){var s,a,i;(function(){var r={},o="[۰-۹]",n=["[کگۀی،","تثجحخد","غيًٌٍَ","ُپٰچژ‌","ء-ةذ-عف-ٔ]"].join(""),l="(،|؟|«|»|؛|٬)",u="(\\.|:|\\!|\\-|\\[|\\]|\\(|\\)|/)";function c(){for(var e="(",t=0;t<arguments.length;t++)e+="(",e+=t!=arguments.length-1?arguments[t]+")|":arguments[t]+")";return e+")"}r.number=new RegExp("^"+o+"+$"),r.letter=new RegExp("^"+n+"+$"),r.punctuation=new RegExp("^"+c(l,u)+"+$"),r.text=new RegExp("^"+c(o,n,l,u,"\\s")+"+$"),r.rtl=new RegExp("^"+c(n,o,l,"\\s")+"+$"),r.hasNumber=new RegExp(o),r.hasLetter=new RegExp(n),r.hasPunctuation=new RegExp(c(l,u)),r.hasText=new RegExp(c(o,n,l,u)),r.hasRtl=new RegExp(c(o,n,l)),r.numbersASCIRange=o,r.lettersASCIRange=n,r.rtlPunctuationsASCIRange=l,r.ltrPunctuationsASCIRange=u,a=[],s=r,i="function"===typeof s?s.apply(t,a):s,void 0===i||(e.exports=i)})()},c66d:function(e,t,r){"use strict";r.r(t);var s=r("7a23"),a=function(e){return Object(s["F"])("data-v-5b867306"),e=e(),Object(s["D"])(),e},i={class:"grid"},o={class:"col-12"},n={class:"card"},l={key:0,class:"text-center"},u={key:1},c={class:"formgrid grid"},d={class:"field col-3"},p={class:"flex align-content-center flex-wrap h-full"},m=["href"],b={class:"hidden"},f={class:"grid"},O={class:"col-12"},h={class:"col-12"},j=a((function(){return Object(s["k"])("div",{class:"field col-9"},null,-1)})),v={class:"formgrid grid"},g={class:"field col"},k={class:"field p-fluid"},y={for:"user._id"},w=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),x={class:"field col"},E={class:"field p-fluid"},P={for:"user.displayName"},N=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),A={class:"formgrid grid"},C={class:"field col"},$={class:"field p-fluid"},R={for:"user.firstName"},I=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),B={class:"field col"},U={class:"field p-fluid"},F={for:"user.lastName"},V=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),q={class:"formgrid grid"},D={class:"field col"},M={class:"field p-fluid"},T={for:"user.mobile"},_=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),S={class:"field col"},K={class:"field p-fluid"},L={for:"user.mail"},z=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),J={class:"formgrid grid"},G={class:"field col"},Z={class:"field p-fluid"},H={for:"user.employeeNumber"},Q=a((function(){return Object(s["k"])("div",{class:"field col"},null,-1)})),W={class:"formgrid grid"},X={class:"field col"},Y={class:"field p-fluid"},ee={for:"user.description"},te={key:0,class:"text-center"},re={key:1},se={class:"formgrid grid"},ae={class:"field col"},ie={class:"field p-fluid"},oe={for:"user.password"},ne=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),le={class:"mt-3"},ue={class:"pl-2 ml-2 mt-0",style:{"line-height":"1.5"}},ce={class:"field col"},de={class:"field p-fluid"},pe={for:"user.passwordRepeat"},me=a((function(){return Object(s["k"])("span",{style:{color:"red"}}," * ",-1)})),be={class:"formgrid grid"},fe={class:"field col"},Oe={class:"field p-fluid"},he={class:"p-inputgroup"},je=a((function(){return Object(s["k"])("span",{id:"countdownButtonCD"},null,-1)})),ve=a((function(){return Object(s["k"])("div",{class:"field col"},null,-1)}));function ge(e,t,r,a,ge,ke){var ye=Object(s["K"])("ProgressSpinner"),we=Object(s["K"])("AppAvatar"),xe=Object(s["K"])("Button"),Ee=Object(s["K"])("InputText"),Pe=Object(s["K"])("Textarea"),Ne=Object(s["K"])("TabPanel"),Ae=Object(s["K"])("Divider"),Ce=Object(s["K"])("Password"),$e=Object(s["K"])("TabView"),Re=Object(s["L"])("tooltip");return Object(s["C"])(),Object(s["j"])("div",i,[Object(s["k"])("div",o,[Object(s["k"])("div",n,[Object(s["k"])("h3",null,Object(s["O"])(e.$t("profile")),1),Object(s["o"])($e,{activeIndex:ge.tabActiveIndex,"onUpdate:activeIndex":t[25]||(t[25]=function(e){return ge.tabActiveIndex=e})},{default:Object(s["U"])((function(){return[Object(s["o"])(Ne,{header:e.$t("editUserInformation")},{default:Object(s["U"])((function(){return[ge.loading?(Object(s["C"])(),Object(s["j"])("div",l,[Object(s["o"])(ye)])):(Object(s["C"])(),Object(s["j"])("div",u,[Object(s["k"])("div",c,[Object(s["k"])("div",d,[Object(s["k"])("div",p,[Object(s["k"])("a",{href:ge.userAvatar,target:"_blank",class:"flex align-items-center justify-content-center avatar"},[Object(s["o"])(we,{image:ge.userAvatar,size:"xlarge",shape:"circle",class:"avatar avatarBorder"},null,8,["image"])],8,m),Object(s["k"])("form",b,[Object(s["k"])("input",{id:"user.avatar",type:"file",name:"file",onChange:t[0]||(t[0]=function(e){return ke.profileRequestMaster("editUserAvatar")})},null,32)]),Object(s["k"])("div",f,[Object(s["k"])("div",O,[Object(s["V"])(Object(s["o"])(xe,{icon:"pi pi-pencil",class:"p-button-rounded mr-2 mb-2",onClick:t[1]||(t[1]=function(e){return ke.editAvatarHelper()})},null,512),[[Re,e.$t("editAvatar"),void 0,{top:!0}]])]),Object(s["k"])("div",h,[Object(s["V"])(Object(s["o"])(xe,{icon:"pi pi-trash",class:"p-button-danger p-button-rounded mr-2 mb-2",onClick:t[2]||(t[2]=function(e){return ke.deleteUserAvatarCheckup()})},null,512),[[Re,e.$t("deleteAvatar"),void 0,{top:!0}]])])])])]),j]),Object(s["k"])("div",v,[Object(s["k"])("div",g,[Object(s["k"])("div",k,[Object(s["k"])("label",y,[Object(s["n"])(Object(s["O"])(e.$t("id")),1),w]),Object(s["o"])(Ee,{id:"user._id",type:"text",class:Object(s["w"])(ge.userErrors._id),modelValue:ge.user._id,"onUpdate:modelValue":t[3]||(t[3]=function(e){return ge.user._id=e}),onKeypress:t[4]||(t[4]=function(e){return ke.englishInputFilter(e)}),onPaste:t[5]||(t[5]=function(e){return ke.englishInputFilter(e)})},null,8,["class","modelValue"]),Object(s["k"])("small",null,Object(s["O"])(e.$t("inputEnglishFilterText")),1)])]),Object(s["k"])("div",x,[Object(s["k"])("div",E,[Object(s["k"])("label",P,[Object(s["n"])(Object(s["O"])(e.$t("persianName")),1),N]),Object(s["o"])(Ee,{id:"user.displayName",type:"text",class:Object(s["w"])(ge.userErrors.displayName),modelValue:ge.user.displayName,"onUpdate:modelValue":t[6]||(t[6]=function(e){return ge.user.displayName=e}),onKeypress:t[7]||(t[7]=function(e){return ke.persianInputFilter(e)}),onPaste:t[8]||(t[8]=function(e){return ke.persianInputFilter(e)})},null,8,["class","modelValue"]),Object(s["k"])("small",null,Object(s["O"])(e.$t("inputPersianFilterText")),1)])])]),Object(s["k"])("div",A,[Object(s["k"])("div",C,[Object(s["k"])("div",$,[Object(s["k"])("label",R,[Object(s["n"])(Object(s["O"])(e.$t("englishFirstName")),1),I]),Object(s["o"])(Ee,{id:"user.firstName",type:"text",class:Object(s["w"])(ge.userErrors.firstName),modelValue:ge.user.firstName,"onUpdate:modelValue":t[9]||(t[9]=function(e){return ge.user.firstName=e}),onKeypress:t[10]||(t[10]=function(e){return ke.englishInputFilter(e)}),onPaste:t[11]||(t[11]=function(e){return ke.englishInputFilter(e)})},null,8,["class","modelValue"]),Object(s["k"])("small",null,Object(s["O"])(e.$t("inputEnglishFilterText")),1)])]),Object(s["k"])("div",B,[Object(s["k"])("div",U,[Object(s["k"])("label",F,[Object(s["n"])(Object(s["O"])(e.$t("englishLastName")),1),V]),Object(s["o"])(Ee,{id:"user.lastName",type:"text",class:Object(s["w"])(ge.userErrors.lastName),modelValue:ge.user.lastName,"onUpdate:modelValue":t[12]||(t[12]=function(e){return ge.user.lastName=e}),onKeypress:t[13]||(t[13]=function(e){return ke.englishInputFilter(e)}),onPaste:t[14]||(t[14]=function(e){return ke.englishInputFilter(e)})},null,8,["class","modelValue"]),Object(s["k"])("small",null,Object(s["O"])(e.$t("inputEnglishFilterText")),1)])])]),Object(s["k"])("div",q,[Object(s["k"])("div",D,[Object(s["k"])("div",M,[Object(s["k"])("label",T,[Object(s["n"])(Object(s["O"])(e.$t("mobile")),1),_]),Object(s["o"])(Ee,{id:"user.mobile",type:"text",class:Object(s["w"])(ge.userErrors.mobile),modelValue:ge.user.mobile,"onUpdate:modelValue":t[15]||(t[15]=function(e){return ge.user.mobile=e})},null,8,["class","modelValue"])])]),Object(s["k"])("div",S,[Object(s["k"])("div",K,[Object(s["k"])("label",L,[Object(s["n"])(Object(s["O"])(e.$t("email")),1),z]),Object(s["o"])(Ee,{id:"user.mail",type:"text",class:Object(s["w"])(ge.userErrors.mail),modelValue:ge.user.mail,"onUpdate:modelValue":t[16]||(t[16]=function(e){return ge.user.mail=e})},null,8,["class","modelValue"])])])]),Object(s["k"])("div",J,[Object(s["k"])("div",G,[Object(s["k"])("div",Z,[Object(s["k"])("label",H,Object(s["O"])(e.$t("employeeNumber")),1),Object(s["o"])(Ee,{id:"user.employeeNumber",type:"text",modelValue:ge.user.employeeNumber,"onUpdate:modelValue":t[17]||(t[17]=function(e){return ge.user.employeeNumber=e})},null,8,["modelValue"])])]),Q]),Object(s["k"])("div",W,[Object(s["k"])("div",X,[Object(s["k"])("div",Y,[Object(s["k"])("label",ee,Object(s["O"])(e.$t("description")),1),Object(s["o"])(Pe,{id:"user.description",modelValue:ge.user.description,"onUpdate:modelValue":t[18]||(t[18]=function(e){return ge.user.description=e}),autoResize:!0,rows:"3"},null,8,["modelValue"])])])]),Object(s["o"])(xe,{id:"user.editButton",label:e.$t("confirm"),class:"p-button-success mt-3 mx-1",onClick:t[19]||(t[19]=function(e){return ke.editUserInformationCheckup()})},null,8,["label"])]))]})),_:1},8,["header"]),Object(s["o"])(Ne,{header:e.$t("editPassword")},{default:Object(s["U"])((function(){return[ge.loading?(Object(s["C"])(),Object(s["j"])("div",te,[Object(s["o"])(ye)])):(Object(s["C"])(),Object(s["j"])("div",re,[Object(s["k"])("div",se,[Object(s["k"])("div",ae,[Object(s["k"])("div",ie,[Object(s["k"])("label",oe,[Object(s["n"])(Object(s["O"])(e.$t("password")),1),ne]),Object(s["o"])(Ce,{id:"user.password",class:Object(s["w"])(ge.userErrors.userPassword),modelValue:ge.user.userPassword,"onUpdate:modelValue":t[20]||(t[20]=function(e){return ge.user.userPassword=e}),toggleMask:!0,autocomplete:"off"},{header:Object(s["U"])((function(){return[Object(s["k"])("h6",null,Object(s["O"])(e.$t("passwordStrength")),1)]})),footer:Object(s["U"])((function(){return[Object(s["o"])(Ae),Object(s["k"])("p",le,Object(s["O"])(e.$t("passwordRequirement")),1),Object(s["k"])("ul",ue,[Object(s["k"])("li",null,Object(s["O"])(e.$t("passwordRequirementText1")),1),Object(s["k"])("li",null,Object(s["O"])(e.$t("passwordRequirementText2")),1),Object(s["k"])("li",null,Object(s["O"])(e.$t("passwordRequirementText3")),1),Object(s["k"])("li",null,Object(s["O"])(e.$t("passwordRequirementText4")),1)])]})),_:1},8,["class","modelValue"])])]),Object(s["k"])("div",ce,[Object(s["k"])("div",de,[Object(s["k"])("label",pe,[Object(s["n"])(Object(s["O"])(e.$t("passwordRepeat")),1),me]),Object(s["o"])(Ce,{id:"user.passwordRepeat",class:Object(s["w"])(ge.userErrors.userPasswordRepeat),modelValue:ge.user.userPasswordRepeat,"onUpdate:modelValue":t[21]||(t[21]=function(e){return ge.user.userPasswordRepeat=e}),toggleMask:!0,onpaste:"return false;",ondrop:"return false;",autocomplete:"off"},null,8,["class","modelValue"])])])]),Object(s["k"])("div",be,[Object(s["k"])("div",fe,[Object(s["k"])("div",Oe,[Object(s["k"])("div",he,[Object(s["V"])(Object(s["o"])(Ee,{modelValue:ge.user.verificationCode,"onUpdate:modelValue":t[22]||(t[22]=function(e){return ge.user.verificationCode=e}),placeholder:e.$t("verificationCode")},null,8,["modelValue","placeholder"]),[[Re,e.$t("profileText1"),void 0,{top:!0}]]),Object(s["o"])(xe,{id:"countdownButton",onClick:t[23]||(t[23]=function(e){return ke.profileRequestMaster("requestVerificationCode")})},{default:Object(s["U"])((function(){return[Object(s["n"])(Object(s["O"])(e.$t("request"))+" ",1),je]})),_:1})])])]),ve]),Object(s["o"])(xe,{label:e.$t("confirm"),class:"p-button-success mx-1",onClick:t[24]||(t[24]=function(e){return ke.editUserPasswordCheckup()})},null,8,["label"])]))]})),_:1},8,["header"])]})),_:1},8,["activeIndex"])])])])}var ke=r("e6da"),ye=r.n(ke),we={name:"Profile",data:function(){return{user:{_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",employeeNumber:"",description:"",userPassword:"",userPasswordRepeat:"",verificationCode:""},userErrors:{_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",userPassword:"",userPasswordRepeat:"",verificationCode:""},userAvatar:"images/avatarPlaceholder.png",verificationCodeCountdown:"",tabActiveIndex:0,loading:!1,persianRex:null}},mounted:function(){this.persianRex=r("bb18"),this.profileRequestMaster("getUserAvatar"),this.profileRequestMaster("getUser")},methods:{profileRequestMaster:function(e){var t=this,r=null;if("Fa"===this.$i18n.locale?r={lang:"fa"}:"En"===this.$i18n.locale&&(r={lang:"en"}),"getUser"===e){this.user={_id:"",displayName:"",firstName:"",lastName:"",mobile:"",mail:"",employeeNumber:"",description:"",userPassword:"",userPasswordRepeat:"",verificationCode:""};var s=new URLSearchParams(r).toString();this.loading=!0,this.axios({url:"/api/user?"+s,method:"GET"}).then((function(e){200===e.data.status.code?(t.loading=!1,t.user._id=e.data.data._id,t.user.displayName=e.data.data.displayName,t.user.firstName=e.data.data.firstName,t.user.lastName=e.data.data.lastName,t.user.mobile=e.data.data.mobile,t.user.mail=e.data.data.mail,t.user.employeeNumber=e.data.data.employeeNumber,t.user.description=e.data.data.description,e.data.data.profileInaccessibility&&(document.getElementById("user._id").readOnly=!0,document.getElementById("user.displayName").readOnly=!0,document.getElementById("user.firstName").readOnly=!0,document.getElementById("user.lastName").readOnly=!0,document.getElementById("user.mobile").readOnly=!0,document.getElementById("user.mail").readOnly=!0,document.getElementById("user.employeeNumber").readOnly=!0,document.getElementById("user.description").readOnly=!0,document.getElementById("user.editButton").style="display: none;")):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))}else if("editUserInformation"===e){var a=new URLSearchParams(r).toString();this.loading=!0,this.axios({url:"/api/user?"+a,method:"PUT",headers:{"Content-Type":"application/json"},data:JSON.stringify({_id:t.user._id,displayName:t.user.displayName,firstName:t.user.firstName,lastName:t.user.lastName,mobile:t.user.mobile,mail:t.user.mail,employeeNumber:t.user.employeeNumber,description:t.user.description}).replace(/\\\\/g,"\\")}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUser")):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))}else if("editUserPassword"===e){var i=new URLSearchParams(r).toString();this.loading=!0,this.axios({url:"/api/user/password?"+i,method:"PUT",headers:{"Content-Type":"application/json"},data:JSON.stringify({newPassword:t.user.userPassword,token:t.user.verificationCode}).replace(/\\\\/g,"\\")}).then((function(e){200===e.data.status.code?(t.loading=!1,t.profileRequestMaster("getUser")):302===e.data.status.code||403===e.data.status.code||429===e.data.status.code?(t.alertPromptMaster(e.data.status.result,"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1):(t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1)})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))}else if("requestVerificationCode"===e){var o=new URLSearchParams(r).toString();this.axios({url:"/api/user/password/request?"+o,method:"GET"}).then((function(e){200===e.data.status.code?(t.setCountdown(60*e.data.data),document.getElementById("countdownButton").disabled=!0):t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA")})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA")}))}else if("getUserAvatar"===e)this.axios({url:"/api/user/photo",method:"GET"}).then((function(e){"Problem"!==e.data&&"NotExist"!==e.data&&(t.userAvatar="/api/user/photo")}));else if("editUserAvatar"===e){var n=new FormData;n.append("file",document.getElementById("user.avatar").files[0]),this.loading=!0,this.axios({url:"/api/user/photo",method:"POST",headers:{"Content-Type":"application/json"},data:n}).then((function(){t.loading=!1,t.profileRequestMaster("getUserAvatar")})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1}))}else"deleteUserAvatar"===e&&(this.loading=!0,this.axios({url:"/api/user/photo",method:"DELETE"}).then((function(){t.loading=!1,t.profileRequestMaster("getUserAvatar")})).catch((function(){t.alertPromptMaster(t.$t("requestError"),"","pi-exclamation-triangle","#FDB5BA"),t.loading=!1})))},alertPromptMaster:function(e,t,r,s){var a=!0;"ltr"===this.$store.state.direction&&(a=!1),ye.a.show({title:e,message:t,position:"center",icon:"pi "+r,backgroundColor:s,transitionIn:"fadeInLeft",rtl:a,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button my-3' style='border-radius: 6px;'>"+this.$t("confirm")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t)}]]})},confirmPromptMaster:function(e,t,r,s,a,i){var o=!0;"ltr"===this.$store.state.direction&&(o=!1),ye.a.show({title:e,message:t,position:"center",icon:"pi "+r,backgroundColor:s,transitionIn:"fadeInLeft",rtl:o,layout:2,timeout:!1,progressBar:!1,buttons:[["<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>"+this.$t("yes")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t),a(i)}],["<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>"+this.$t("no")+"</button>",function(e,t){e.hide({transitionOut:"fadeOutRight"},t)}]]})},editUserInformationCheckup:function(){var e=0,t=/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,r=/^(\+98|0)?9\d{9}$/;""===this.user._id?(this.userErrors._id="p-invalid",e+=1):this.userErrors._id="",""===this.user.displayName?(this.userErrors.displayName="p-invalid",e+=1):this.userErrors.displayName="",""===this.user.firstName?(this.userErrors.firstName="p-invalid",e+=1):this.userErrors.firstName="",""===this.user.lastName?(this.userErrors.lastName="p-invalid",e+=1):this.userErrors.lastName="",""===this.user.mobile?(this.userErrors.mobile="p-invalid",e+=1):r.test(Number(this.user.mobile))?this.userErrors.mobile="":(this.userErrors.mobile="p-invalid",e+=1),""===this.user.mail?(this.userErrors.mail="p-invalid",e+=1):t.test(this.user.mail)?this.userErrors.mail="":(this.userErrors.mail="p-invalid",e+=1),e>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("editUserInformation"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"editUserInformation")},editUserPasswordCheckup:function(){var e=0,t=/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/;""===this.user.verificationCode?(this.userErrors.verificationCode="p-invalid",e+=1):this.userErrors.verificationCode="",""===this.user.userPassword?(this.userErrors.userPassword="p-invalid",e+=1):""===this.user.userPasswordRepeat?(this.userErrors.userPasswordRepeat="p-invalid",e+=1):this.user.userPassword!==this.user.userPasswordRepeat?(this.userErrors.userPassword="p-invalid",this.userErrors.userPasswordRepeat="p-invalid",e+=1):t.test(this.user.userPassword)?(this.userErrors.userPassword="",this.userErrors.userPasswordRepeat=""):(this.userErrors.userPassword="p-invalid",e+=1),e>0?this.alertPromptMaster(this.$t("invalidInputsError"),"","pi-exclamation-triangle","#FDB5BA"):this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("editPassword"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"editUserPassword")},deleteUserAvatarCheckup:function(){this.confirmPromptMaster(this.$t("confirmPromptText"),this.$t("deleteAvatar"),"pi-question-circle","#F0EAAA",this.profileRequestMaster,"deleteUserAvatar")},editAvatarHelper:function(){document.getElementById("user.avatar").click()},setCountdown:function(e){var t,r,s=e,a=setInterval((function(){t=parseInt(s/60,10),r=parseInt(s%60,10),t=t<10?"0"+t:t,r=r<10?"0"+r:r,document.querySelector("#countdownButtonCD").textContent=" ("+t+":"+r+") ",--s<0&&(clearInterval(a),document.querySelector("#countdownButtonCD").textContent="",document.getElementById("countdownButton").disabled=!1)}),1e3)},englishInputFilter:function(e){if("keypress"===e.type){var t=e.keyCode?e.keyCode:e.which;(t<48||t>122||t>57&&t<65||t>90&&t<97)&&e.preventDefault()}else if("paste"===e.type)for(var r=e.clipboardData.getData("text"),s=0;s<r.length;++s){if(r[s].charCodeAt(0)<48||r[s].charCodeAt(0)>122){e.preventDefault();break}if(r[s].charCodeAt(0)>57&&r[s].charCodeAt(0)<65){e.preventDefault();break}if(r[s].charCodeAt(0)>90&&r[s].charCodeAt(0)<97){e.preventDefault();break}}},persianInputFilter:function(e){if("keypress"===e.type){var t=e.key?e.key:e.which,r=e.keyCode?e.keyCode:e.which;(r<48||r>57)&&(r>32&&r<65||r>90&&r<97||r>122&&r<127?e.preventDefault():this.persianRex.text.test(t)||e.preventDefault())}else if("paste"===e.type)for(var s=e.clipboardData.getData("text"),a=0;a<s.length;++a)if(s[a].charCodeAt(0)<48||s[a].charCodeAt(0)>57){if(s[a].charCodeAt(0)>32&&s[a].charCodeAt(0)<65){e.preventDefault();break}if(s[a].charCodeAt(0)>90&&s[a].charCodeAt(0)<97){e.preventDefault();break}if(s[a].charCodeAt(0)>122&&s[a].charCodeAt(0)<127){e.preventDefault();break}if(!this.persianRex.text.test(s[a])){e.preventDefault();break}}}}},xe=(r("914c"),r("6b0d")),Ee=r.n(xe);const Pe=Ee()(we,[["render",ge],["__scopeId","data-v-5b867306"]]);t["default"]=Pe}}]);
//# sourceMappingURL=chunk-1e6cdaf8.04fd93f5.js.map