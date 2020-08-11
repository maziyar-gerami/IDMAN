function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      var i;
      for (i = 0; i < dropdowns.length; ++i) {
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
  new Vue({
    router,
    el: '#app',
    data: {
      userInfo: [],
      username: "",
      name: "",
      nameEN: "",
      groups: [],
      groupList: "",
      margin: "margin-right: 30px;",
      lang: "EN",
      isRtl: true,
      samls: "display: none;",
      userPicture: "images/PlaceholderUser.png",
      s0: "پارسو",
      s1: "",
      s2: "خروج",
      s3: "داشبورد",
      s4: "سرویس ها",
      s5: "گروه ها",
      s6: "رویداد ها",
      s7: "پروفایل",
      s8: "ایجاد سرویس جدید",
      s9: "fa fa-arrow-right",
      s10: "قوانین",
      s11: "حریم خصوصی",
      s12: "راهنما",
      s13: "کاربران",
      s14: "تنطیمات پایه",
      s15: "فعال سازی سرویس",
      s16: "نوع سرویس",
      s17: "نام سرویس",
      s18: "آدرس سرویس",
      s19: "معرفی سرویس",
      s20: "پیوندهای مرجع",
      s21: "آدرس لوگو",
      s22: "آدرس راهنما",
      s23: "آدرس قوانین",
      s24: "اطلاعات تماس",
      s25: "نام",
      s26: "ایمیل",
      s27: "شماره تماس",
      s28: "دپارتمان",
      s29: "تنطیمات خروج",
      s30: "آدرس خروج",
      s31: "نوع خروج",
      s32: "تایید",
      s33: "./dashboard",
      s34: "./services",
      s35: "گروه ها",
      s36: "شناسه سرویس",
      s37: "./users",
      s38: "./groups",
      s39: "./settings",
      s40: "./privacy",
      s41: "پیکربندی",
      s42: "./configs",
      s43: "./events"
    },
    created: function () {
      this.getGroups();
      this.getUserInfo();
      this.getUserPic();
      if(typeof this.$route.query.en !== 'undefined'){
        this.changeLang();
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
      getGroups: function () {
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port  + "/api/groups";
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
          this.s8 = "Create New Service";
          this.s9 = "fa fa-arrow-left";
          this.s10 = "Rules";
          this.s11 = "Privacy";
          this.s12 = "Guide";
          this.s13 = "Users";
          this.s14 = "Basics";
          this.s15 = "Enable Service";
          this.s16 = "Service Type";
          this.s17 = "Service Name";
          this.s18 = "Service URL";
          this.s19 = "Description";
          this.s20 = "Reference Links";
          this.s21 = "Logo URL";
          this.s22 = "Information URL";
          this.s23 = "Privacy URL";
          this.s24 = "Contacts";
          this.s25 = "Name";
          this.s26 = "Email";
          this.s27 = "Phone";
          this.s28 = "Department";
          this.s29 = "Logout Options";
          this.s30 = "Logout URL";
          this.s31 = "Logout Type";
          this.s32 = "Submit";
          this.s33 = "./dashboard?en";
          this.s34 = "./services?en";
          this.s35 = "Groups";
          this.s36 = "ServiceID";
          this.s37 = "./users?en";
          this.s38 = "./groups?en";
          this.s39 = "./settings?en";
          this.s40 = "./privacy?en";
          this.s41 = "Configs";
          this.s42 = "./configs?en";
          this.s43 = "./events?en";
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
            this.s8 = "ایجاد سرویس جدید";
            this.s9 = "fa fa-arrow-right";
            this.s10 = "قوانین";
            this.s11 = "حریم خصوصی";
            this.s12 = "راهنما";
            this.s13 = "کاربران";
            this.s14 = "تنطیمات پایه";
            this.s15 = "فعال سازی سرویس";
            this.s16 = "نوع سرویس";
            this.s17 = "نام سرویس";
            this.s18 = "آدرس سرویس";
            this.s19 = "معرفی سرویس";
            this.s20 = "پیوندهای مرجع";
            this.s21 = "آدرس لوگو";
            this.s22 = "آدرس راهنما";
            this.s23 = "آدرس قوانین";
            this.s24 = "اطلاعات تماس";
            this.s25 = "نام";
            this.s26 = "ایمیل";
            this.s27 = "شماره تماس";
            this.s28 = "دپارتمان";
            this.s29 = "تنطیمات خروج";
            this.s30 = "آدرس خروج";
            this.s31 = "نوع خروج";
            this.s32 = "تایید";
            this.s33 = "./dashboard";
            this.s34 = "./services";
            this.s35 = "گروه ها";
            this.s36 = "شناسه سرویس";
            this.s37 = "./users";
            this.s38 = "./groups";
            this.s39 = "./settings";
            this.s40 = "./privacy";
            this.s41 = "پیکربندی";
            this.s42 = "./configs";
            this.s43 = "./events";
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
      }
  })
})