function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
  }
  window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
          var openDropdown = dropdowns[i];
          if (openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
          }
        }
    }
  } 
  document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
      mode: 'history',
      routes: []
    });
    Vue.component('v-pagination', window['vue-plain-pagination'])
    new Vue({
      router,
      el: '#app',
      data: {
        recordsShownOnPage: 20,
        userInfo: [],
        username: "",
        name: "",
        nameEN: "",
        services: [],
        servicesPage: [],
        message: "",
        editInfo: {},
        placeholder: "text-align: right;",
        margin: "margin-right: 30px;",
        lang: "EN",
        isRtl: true,
        groups: [],
        groupList: "",
        samls: "display: none;",
        editS: "display: none;",
        editS1: "display: none;",
        editS2: "display: none;",
        showS: "",
        flag: false,
        currentPage: 1,
        total: 1,
        bootstrapPaginationClasses: {
            ul: 'pagination',
            li: 'page-item',
            liActive: 'active',
            liDisable: 'disabled',
            button: 'page-link'  
        },
        paginationAnchorTexts: {
            first: '<<',
            prev: '<',
            next: '>',
            last: '>>'
        },
        userPicture: "images/PlaceholderUser.png",
        s0: "پارسو",
        s1: "",
        s2: "خروج",
        s3: "داشبورد",
        s4: "سرویس ها",
        s5: "گروه ها",
        s6: "رویداد ها",
        s7: "پروفایل",
        s8: "سرویس های متصل",
        s9: "سرویس ها",
        s10: "قوانین",
        s11: "حریم خصوصی",
        s12: "راهنما",
        s13: "ایجاد سرویس جدید",
        s14: "سرویس جدید",
        s15: "کاربران",
        s16: "./dashboard",
        s17: "./services",
        s18: "./createservice",
        s19: "./users",
        s20: "شناسه",
        s21: "نام",
        s22: "آدرس",
        s23: "ویرایش",
        s24: "حذف",
        s25: "تنطیمات پایه",
        s26: "فعال سازی سرویس",
        s27: "نوع سرویس",
        s28: "نام سرویس",
        s29: "آدرس سرویس",
        s30: "شناسه سرویس",
        s31: "معرفی سرویس",
        s32: "گروه ها",
        s33: "پیوندهای مرجع",
        s34: "آدرس لوگو",
        s35: "آدرس راهنما",
        s36: "آدرس قوانین",
        s37: "اطلاعات تماس",
        s38: "نام",
        s39: "ایمیل",
        s40: "شماره تماس",
        s41: "دپارتمان",
        s42: "تنطیمات خروج",
        s43: "آدرس خروج",
        s44: "نوع خروج",
        s45: "تایید",
        s46: "بازگشت",
        s47: "حذف تمامی سرویس ها",
        s48: "./groups",
        s49: "./settings",
        s50: "./privacy",
        s51: "پیکربندی",
        s52: "./configs",
        s53: "./events",
      },
      created: function () {
        this.getUserInfo();
        this.getUserPic();
        this.refreshServices();
        this.getGroups();
        if(typeof this.$route.query.en !== 'undefined'){
          this.changeLang()
        }
      },
      methods: {
        getUserInfo: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/user") //
            .then((res) => {
                vm.userInfo = res.data;
                vm.username = vm.userInfo.userId;
                vm.name = vm.userInfo.displayName;
                vm.nameEN = vm.userInfo.firstName + vm.userInfo.lastName;
                vm.s1 = vm.name;
            });
        },
        getUserPic: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/user/photo") //
              .then((res) => {
                vm.userPicture = "/api/user/photo";
              })
              .catch((error) => {
                  if (error.response) {
                    if (error.response.status == 400 || error.response.status == 500) {
                      vm.userPicture = "images/PlaceholderUser.png";
                    }else{
                      vm.userPicture = "/api/user/photo";
                    }
                  }else{
                    console.log("error.response is False")
                  }
              });
        },
        refreshServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + "/api/services") //
          .then((res) => {
            vm.services = res.data;
            vm.total = Math.ceil(vm.services.length / vm.recordsShownOnPage);
          });
        },
        showServices: function () {
          document.getElementById("editS").submit();
          this.showS = ""
          this.editS = "display:none"
        },
        editServiceS: function (id) {
          document.getElementById("showS0").setAttribute("style", "display:none;");
          document.getElementById("showS1").setAttribute("style", "display:none;");
          document.getElementById("showS2").setAttribute("style", "display:none;");
          document.getElementById("showS3").setAttribute("style", "display:none;");
          document.getElementById("showS4").setAttribute("style", "display:none;");
          document.getElementById("showS5").setAttribute("style", "display:none;");
          document.getElementById("editS").setAttribute("style", "");
          document.getElementById("editS1").setAttribute("style", "");
          document.getElementById("editS2").setAttribute("style", "");
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.get(url + `/api/services/${id}`) //
          .then((res) => {
            vm.flag = true;
            vm.editInfo = res.data;
            document.getElementsByName("id")[0].setAttribute("value", vm.editInfo.id);
            if(typeof vm.editInfo.accessStrategy.enabled !== 'undefined'){
              if(vm.editInfo.accessStrategy.enabled){
                document.getElementsByName("enabled")[0].checked = true;
              }else{
                document.getElementsByName("enabled")[0].checked = false;
              }
            }
            if(typeof vm.editInfo.name !== 'undefined'){
              document.getElementsByName("name")[0].setAttribute("value", vm.editInfo.name);
            }
            if(typeof vm.editInfo.accessStrategy.endpointUrl !== 'undefined'){
              document.getElementsByName("endpointUrl")[0].setAttribute("value", vm.editInfo.accessStrategy.endpointUrl);
            }
            if(typeof vm.editInfo.serviceId !== 'undefined'){
              document.getElementsByName("serviceId")[0].setAttribute("value", vm.editInfo.serviceId.replace(".+", "").replace("^", ""));
            }
            if(typeof vm.editInfo.description !== 'undefined'){
              document.getElementsByName("description")[0].setAttribute("value", vm.editInfo.description);
            }
            if(typeof vm.editInfo.logo !== 'undefined'){
              document.getElementsByName("logo")[0].setAttribute("value", vm.editInfo.logo);
            }
            if(typeof vm.editInfo.informationUrl !== 'undefined'){
              document.getElementsByName("informationUrl")[0].setAttribute("value", vm.editInfo.informationUrl);
            }
            if(typeof vm.editInfo.privacyUrl !== 'undefined'){
              document.getElementsByName("privacyUrl")[0].setAttribute("value", vm.editInfo.privacyUrl);
            }
            if(typeof vm.editInfo.metadataLocation !== 'undefined'){
              document.getElementById("metadataLocation").setAttribute("value", vm.editInfo.metadataLocation);
            }
            if(typeof vm.editInfo.metadataMaxValidity !== 'undefined'){
              document.getElementById("metadataMaxValidity").setAttribute("value", vm.editInfo.metadataMaxValidity);
            }
            if(typeof vm.editInfo.metadataSignatureLocation !== 'undefined'){
              document.getElementById("metadataSignatureLocation").setAttribute("value", vm.editInfo.metadataSignatureLocation);
            }
            if(typeof vm.editInfo.metadataExpirationDuration !== 'undefined'){
              document.getElementById("metadataExpirationDuration").setAttribute("value", vm.editInfo.metadataExpirationDuration);
            }
            if(typeof vm.editInfo.metadataCriteriaPattern !== 'undefined'){
              document.getElementById("metadataCriteriaPattern").setAttribute("value", vm.editInfo.metadataCriteriaPattern);
            }
            if(typeof vm.editInfo.metadataCriteriaDirection !== 'undefined'){
              if(vm.editInfo.metadataCriteriaDirection == "INCLUDE"){
                  document.getElementById("option2").selected = false;
                  document.getElementById("option3").selected = true;
              }else if(vm.editInfo.metadataCriteriaDirection == "EXCLUDE"){
                  document.getElementById("option2").selected = false;
                  document.getElementById("option4").selected = true;
              }
            }
            if(typeof vm.editInfo.metadataCriteriaRoles !== 'undefined'){
              document.getElementById("metadataCriteriaRoles").setAttribute("value", vm.editInfo.metadataCriteriaRoles);
            }
            if(typeof vm.editInfo.metadataCriteriaRemoveEmptyEntitiesDescriptors !== 'undefined'){
              if(vm.editInfo.metadataCriteriaRemoveEmptyEntitiesDescriptors){
                document.getElementById("metadataCriteriaRemoveEmptyEntitiesDescriptors").checked = true;
              }else{
                document.getElementById("metadataCriteriaRemoveEmptyEntitiesDescriptors").checked = false;
              }
            }
            if(typeof vm.editInfo.metadataCriteriaRemoveRolelessEntityDescriptors !== 'undefined'){
              if(vm.editInfo.metadataCriteriaRemoveRolelessEntityDescriptors){
                document.getElementById("metadataCriteriaRemoveRolelessEntityDescriptors").checked = true;
              }else{
                document.getElementById("metadataCriteriaRemoveRolelessEntityDescriptors").checked = false;
              }
            }
            if(typeof vm.editInfo.signAssertions !== 'undefined'){
              if(vm.editInfo.signAssertions){
                document.getElementById("signAssertions").checked = true;
              }else{
                document.getElementById("signAssertions").checked = false;
              }
            }
            if(typeof vm.editInfo.signResponses !== 'undefined'){
              if(vm.editInfo.signResponses){
                document.getElementById("signResponses").checked = true;
              }else{
                document.getElementById("signResponses").checked = false;
              }
            }
            if(typeof vm.editInfo.encryptAssertions !== 'undefined'){
              if(vm.editInfo.encryptAssertions){
                document.getElementById("encryptAssertions").checked = true;
              }else{
                document.getElementById("encryptAssertions").checked = false;
              }
            }
            if(typeof vm.editInfo.signingCredentialType !== 'undefined'){
              if(vm.editInfo.signingCredentialType == "X509"){
                document.getElementById("option5").selected = false;
                document.getElementById("option6").selected = true;
              }
            }
            if(typeof vm.editInfo.requiredAuthenticationContextClass !== 'undefined'){
              document.getElementById("requiredAuthenticationContextClass").setAttribute("value", vm.editInfo.requiredAuthenticationContextClass);
            }
            if(typeof vm.editInfo.assertionAudiences !== 'undefined'){
              document.getElementById("assertionAudiences").setAttribute("value", vm.editInfo.assertionAudiences);
            }
            if(typeof vm.editInfo.contacts[1][0].name !== 'undefined'){
              document.getElementsByName("cname")[0].setAttribute("value", vm.editInfo.contacts[1][0].name);
            }
            if(typeof vm.editInfo.contacts[1][0].email !== 'undefined'){
              document.getElementsByName("email")[0].setAttribute("value", vm.editInfo.contacts[1][0].email);
            }
            if(typeof vm.editInfo.contacts[1][0].phone !== 'undefined'){
              document.getElementsByName("phone")[0].setAttribute("value", vm.editInfo.contacts[1][0].phone);
            }
            if(typeof vm.editInfo.contacts[1][0].department !== 'undefined'){
              document.getElementsByName("department")[0].setAttribute("value", vm.editInfo.contacts[1][0].department);
            }
            if(typeof vm.editInfo.logoutUrl !== 'undefined'){
              document.getElementsByName("logoutUrl")[0].setAttribute("value", vm.editInfo.logoutUrl);
            }
            if(typeof vm.editInfo.logoutType !== 'undefined'){
              if(vm.editInfo.logoutType == "NONE"){
                  document.getElementById("option8").selected = false;
                  document.getElementById("option7").selected = true;
              }else if(vm.editInfo.logoutType == "FRONT_CHANNEL"){
                  document.getElementById("option8").selected = false;
                  document.getElementById("option9").selected = true;
              }
            }
            if(typeof vm.editInfo.accessStrategy.requiredAttributes.member !== 'undefined'){
              for(let i = 0; i < vm.editInfo.accessStrategy.requiredAttributes.member[1].length; ++i){
                vm.editInfo.accessStrategy.requiredAttributes.member[1][i]
                document.getElementById("groupNameId" + vm.editInfo.accessStrategy.requiredAttributes.member[1][i]).checked = true;
                if(vm.groupList === ""){
                  vm.groupList += vm.editInfo.accessStrategy.requiredAttributes.member[1][i];
                }else{
                  vm.groupList += ',' + vm.editInfo.accessStrategy.requiredAttributes.member[1][i];
                }
              }
            }
          });
        },
        editService: function () {
          document.getElementById("showS0").setAttribute("style", "");
          document.getElementById("showS1").setAttribute("style", "");
          document.getElementById("showS2").setAttribute("style", "");
          document.getElementById("showS3").setAttribute("style", "");
          document.getElementById("showS4").setAttribute("style", "");
          document.getElementById("showS5").setAttribute("style", "");
          document.getElementById("editS").setAttribute("style", "display:none;");
          document.getElementById("editS1").setAttribute("style", "display:none;");
          document.getElementById("editS2").setAttribute("style", "display:none;");
          location.reload();
        },
        deleteService: function (id) {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.delete(url + `/api/services/${id}`) //
          .then(() => {
            location.reload();
          });
        },
        deleteAllServices: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port;
          var vm = this;
          axios.delete(url + "/api/services") //
          .then(() => {
            location.reload();
          });
        },
        getGroups: function () {
          var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port  + "/api/groups"; //
          var vm = this;
          axios.get(url)
          .then((res) => {
            vm.groups = res.data;
            for(var i = 0; i < vm.groups.length; ++i){
              vm.groups[i].id = vm.groups[i].name;
            }
          });
        },
        addGroup: function (n) {
          n = n.split("'").join("");
          if(this.groupList.includes(n)){
            if(this.groupList.includes(n + ',')){
              this.groupList = this.groupList.replace(n + ',', "");
            }else if(this.groupList === n){
              this.groupList = this.groupList.replace(n, "");
            }else{
              this.groupList = this.groupList.replace(',' + n, "");
            }
          }else{
            if(this.groupList === ""){
              this.groupList += n;
            }else{
              this.groupList += ',' + n;
            }
          }
        },
        changeLang: function () {
          if(this.lang == "EN"){
            this.margin = "margin-left: 30px;";
            this.lang = "فارسی";
            this.isRtl = false;
            this.s0 = "Parsso";
            this.s1 = this.nameEN;
            this.s2 = "Exit";
            this.s3 = "Dashboard";
            this.s4 = "Services";
            this.s5 = "Groups";
            this.s6 = "Events";
            this.s7 = "Profile";
            this.s8 = "Connected Services";
            this.s9 = "Services";
            this.s10 = "Rules";
            this.s11 = "Privacy";
            this.s12 = "Guide";
            this.s13 = "Create New Service";
            this.s14 = "New Service";
            this.s15 = "Users";
            this.s16 = "./dashboard?en";
            this.s17 = "./services?en";
            this.s18 = "./createservice?en";
            this.s19 = "./users?en";
            this.s20 = "ID";
            this.s21 = "Name";
            this.s22 = "URL";
            this.s23 = "Edit";
            this.s24 = "Delete";
            this.s25 = "Basics";
            this.s26 = "Enable Service";
            this.s27 = "Service Type";
            this.s28 = "Service Name";
            this.s29 = "Service URL";
            this.s30 = "ServiceID";
            this.s31 = "Description";
            this.s32 = "Groups";
            this.s33 = "Reference Links";
            this.s34 = "Logo URL";
            this.s35 = "Information URL";
            this.s36 = "Privacy URL";
            this.s37 = "Contacts";
            this.s38 = "Name";
            this.s39 = "Email";
            this.s40 = "Phone";
            this.s41 = "Department";
            this.s42 = "Logout Options";
            this.s43 = "Logout URL";
            this.s44 = "Logout Type";
            this.s45 = "Submit";
            this.s46 = "Go Back";
            this.s47 = "Remove All Services";
            this.s48 = "./groups?en";
            this.s49 = "./settings?en";
            this.s50 = "./privacy?en";
            this.s51 = "Configs";
            this.s52 = "./configs?en";
            this.s53 = "./events?en";
          } else{
              this.margin = "margin-right: 30px;";
              this.lang = "EN";
              this.isRtl = true;
              this.s0 = "پارسو";
              this.s1 = this.name;
              this.s2 = "خروج";
              this.s3 = "داشبورد";
              this.s4 = "سرویس ها";
              this.s5 = "گروه ها";
              this.s6 = "رویداد ها";
              this.s7 = "پروفایل";
              this.s8 = "سرویس های متصل";
              this.s9 = "سرویس ها";
              this.s10 = "قوانین";
              this.s11 = "حریم خصوصی";
              this.s12 = "راهنما";
              this.s13 = "ایجاد سرویس جدید";
              this.s14 = "سرویس جدید";
              this.s15 = "کاربران";
              this.s16 = "./dashboard";
              this.s17 = "./services";
              this.s18 = "./createservice";
              this.s19 = "./users";
              this.s20 = "شناسه";
              this.s21 = "نام";
              this.s22 = "آدرس";
              this.s23 = "ویرایش";
              this.s24 = "حذف";
              this.s25 = "تنطیمات پایه";
              this.s26 = "فعال سازی سرویس";
              this.s27 = "نوع سرویس";
              this.s28 = "نام سرویس";
              this.s29 = "آدرس سرویس";
              this.s30 = "شناسه سرویس";
              this.s31 = "معرفی سرویس";
              this.s32 = "گروه ها";
              this.s33 = "پیوندهای مرجع";
              this.s34 = "آدرس لوگو";
              this.s35 = "آدرس راهنما";
              this.s36 = "آدرس قوانین";
              this.s37 = "اطلاعات تماس";
              this.s38 = "نام";
              this.s39 = "ایمیل";
              this.s40 = "شماره تماس";
              this.s41 = "دپارتمان";
              this.s42 = "تنطیمات خروج";
              this.s43 = "آدرس خروج";
              this.s44 = "نوع خروج";
              this.s45 = "تایید";
              this.s46 = "بازگشت";
              this.s47 = "حذف تمامی سرویس ها";
              this.s48 = "./groups";
              this.s49 = "./settings";
              this.s50 = "./privacy";
              this.s51 = "پیکربندی";
              this.s52 = "./configs";
              this.s53 = "./events";
          }
        },
        saml: function () {
          this.samls = "";
          this.s18 = "Entity ID";
          document.getElementById("metadataLocation").required = true;
          document.getElementById("metadataLocation").setAttribute("name", "metadataLocation");
          document.getElementById("metadataMaxValidity").setAttribute("name", "metadataMaxValidity");
          document.getElementById("metadataSignatureLocation").setAttribute("name", "metadataSignatureLocation");
          document.getElementById("metadataExpirationDuration").setAttribute("name", "metadataExpirationDuration");
          document.getElementById("metadataCriteriaPattern").setAttribute("name", "metadataCriteriaPattern");
          document.getElementById("metadataCriteriaDirection").setAttribute("name", "metadataCriteriaDirection");
          document.getElementById("metadataCriteriaRoles").setAttribute("name", "metadataCriteriaRoles");
          document.getElementById("metadataCriteriaRemoveEmptyEntitiesDescriptors").setAttribute("name", "metadataCriteriaRemoveEmptyEntitiesDescriptors");
          document.getElementById("metadataCriteriaRemoveRolelessEntityDescriptors").setAttribute("name", "metadataCriteriaRemoveRolelessEntityDescriptors");
          document.getElementById("signAssertions").setAttribute("name", "signAssertions");
          document.getElementById("signResponses").setAttribute("name", "signResponses");
          document.getElementById("encryptAssertions").setAttribute("name", "encryptAssertions");
          document.getElementById("signingCredentialType").setAttribute("name", "signingCredentialType");
          document.getElementById("requiredAuthenticationContextClass").setAttribute("name", "requiredAuthenticationContextClass");
          document.getElementById("assertionAudiences").setAttribute("name", "assertionAudiences");
          },
        cas: function () {
          this.samls = "display: none;";
          if(this.lang == "EN"){
            this.s18 = "آدرس سرویس";
          }else{
            this.s18 = "Service URL";
          }
          document.getElementById("metadataLocation").removeAttribute("required");
          document.getElementById("metadataLocation").removeAttribute("name");
          document.getElementById("metadataMaxValidity").removeAttribute("name");
          document.getElementById("metadataSignatureLocation").removeAttribute("name");
          document.getElementById("metadataExpirationDuration").removeAttribute("name");
          document.getElementById("metadataCriteriaPattern").removeAttribute("name");
          document.getElementById("metadataCriteriaDirection").removeAttribute("name");
          document.getElementById("metadataCriteriaRoles").removeAttribute("name");
          document.getElementById("metadataCriteriaRemoveEmptyEntitiesDescriptors").removeAttribute("name");
          document.getElementById("metadataCriteriaRemoveRolelessEntityDescriptors").removeAttribute("name");
          document.getElementById("signAssertions").removeAttribute("name");
          document.getElementById("signResponses").removeAttribute("name");
          document.getElementById("encryptAssertions").removeAttribute("name");
          document.getElementById("signingCredentialType").removeAttribute("name");
          document.getElementById("requiredAuthenticationContextClass").removeAttribute("name");
          document.getElementById("assertionAudiences").removeAttribute("name");
          }
      },
      computed:{
        sortedServices:function() {
          this.servicesPage = [];
          for(let i = 0; i < this.recordsShownOnPage; ++i){
              if(i + ((this.currentPage - 1) * this.recordsShownOnPage) <= this.services.length - 1){
                  this.servicesPage[i] = this.services[i + ((this.currentPage - 1) * this.recordsShownOnPage)];
              }
          }
          return this.servicesPage;
        }
      }
    });
  })