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
    new Vue({
      el: '#app',
      data: {
        margin: "margin-right: 30px;",
        lang: "EN",
        isRtl: true,
        s0: "پارسو",
        s1: "بردیا اکبری",
        s2: "خروج",
        s3: "داشبورد",
        s4: "سرویس ها",
        s5: "گروه ها",
        s6: "رویداد ها",
        s7: "تنظیمات",
        s8: "ایجاد سرویس جدید",
        s9: "خلاصه وضعیت",
        s10: "قوانین",
        s11: "حریم خصوصی",
        s12: "راهنما",
        s13: "کاربران"
      },
      methods: {
        changeLang: function () {
          if(this.lang == "EN"){
            this.margin = "margin-left: 30px;";
            this.lang = "فارسی";
            this.isRtl = false;
            this.s0 = "Parsso";
            this.s1 = "Bardia Akbari";
            this.s2 = "Exit";
            this.s3 = "Dashboard";
            this.s4 = "Services";
            this.s5 = "Groups";
            this.s6 = "Events";
            this.s7 = "Settings";
            this.s8 = "ایجاد سرویس جدید";
            this.s9 = "Status Summary";
            this.s10 = "Rules";
            this.s11 = "Privacy";
            this.s12 = "Guide";
            this.s13 = "Users";
          } else{
              this.margin = "margin-right: 30px;";
              this.lang = "EN";
              this.isRtl = true;
              this.s0 = "پارسو";
              this.s1 = "بردیا اکبری";
              this.s2 = "خروج";
              this.s3 = "داشبورد";
              this.s4 = "سرویس ها";
              this.s5 = "گروه ها";
              this.s6 = "رویداد ها";
              this.s7 = "تنظیمات";
              this.s8 = "ایجاد سرویس جدید";
              this.s9 = "خلاصه وضعیت";
              this.s10 = "قوانین";
              this.s11 = "حریم خصوصی";
              this.s12 = "راهنما";
              this.s13 = "کاربران";
          }
        }
      }
    })
  })