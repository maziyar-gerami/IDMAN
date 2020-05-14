document.addEventListener('DOMContentLoaded', function () {
    new Vue({
      el: '#app',
      data: {
        placeholder: "text-align: right;",
        lang: "EN",
        s0: "پارسو",
        s1: "نام کاربری و رمز عبور خود را وارد کنید",
        s2: "نام کاربری",
        s3: "رمز عبور",
        s4: "ورود"
      },
      methods: {
        changeLang: function () {
          if(this.lang == "EN"){
              this.placeholder = "text-align: left;"
              this.lang  = "فارسی";
              this.s0 = "Parsso";
              this.s1 = "Enter Your Username and Password";
              this.s2 = "Username";
              this.s3 = "Password";
              this.s4 = "Sign in";
          } else{
            this.placeholder = "text-align: right;"
              this.lang  = "EN";
              this.s0 = "پارسو";
              this.s1 = "نام کاربری و رمز عبور خود را وارد کنید";
              this.s2 = "نام کاربری";
              this.s3 = "رمز عبور";
              this.s4 = "ورود";
          }
        }
      }
    })
  })