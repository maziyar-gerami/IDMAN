document.addEventListener('DOMContentLoaded', function () {
  var router = new VueRouter({
    mode: 'history',
    routes: []
  });  
  new Vue({
      router,
      el: '#app',
      data: {
        placeholder: "text-align: right;",
        lang: "EN",
        s0: "پارسو",
        s1: "نام کاربری و رمز عبور خود را وارد کنید",
        s2: "نام کاربری",
        s3: "رمز عبور",
        s4: "ورود",
        s5: "اطلاعات کاربری نادرست است"
      },
      mounted: function() {
        if(typeof this.$route.query.en !== 'undefined'){
          this.changeLang()
        }
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
              this.s5 = "Invalid Username and Password";
          } else{
            this.placeholder = "text-align: right;"
              this.lang  = "EN";
              this.s0 = "پارسو";
              this.s1 = "نام کاربری و رمز عبور خود را وارد کنید";
              this.s2 = "نام کاربری";
              this.s3 = "رمز عبور";
              this.s4 = "ورود";
              this.s5 = "اطلاعات کاربری نادرست است";
          }
        }
      }
    })
  })