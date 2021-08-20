document.addEventListener('DOMContentLoaded', function () {
    var router = new VueRouter({
        mode: 'history',
        routes: []
    });
    new Vue({
        router,
        el: '#app',
        data: {
            dateNav: "",
            dateNavEn: "",
            dateNavText: "",
            show: false,
            showR: false,
            isRtl: true,
            placeholder: "text-align: right;",
            margin: "margin-right: 30px;",
            marg: "margin-left: auto;",
            font: "font-size: 0.74em; text-align: right;",
            lang: "EN",
            rtl: "direction: rtl;",
            eye: "right: 3%;",
            s0: "پارسو",
            s1: "نام کاربری و رمز عبور خود را وارد کنید",
            s2: "نام کاربری",
            s3: "رمز عبور",
            s4: "ورود",
            s5: "اطلاعات کاربری نادرست است",
            s6: "رمز عبور جدید",
            s7: "تکرار رمز عبور جدید",
            s8: "تایید",
            s9: "رمز عبور های وارد شده یکسان نمی باشند",
            s10: ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید",
            s11: "ارسال ایمیل باز نشانی",
            s12: "بازنشانی رمز عبور",
            s13: ":رمز عبور شما باید شامل موارد زیر باشد",
            s14: ":جهت تکمیل فرآیند بازنشانی رمز عبور خود، با رعایت نکات زیر، رمز عبور جدید خود را وارد نمایید",
            s15: "شما اجازه دسترسی به این صفحه را ندارید",
            s16: "بازگشت",
            s17: "",
            s18: " عزیز",
            s19: "،",
            s20: "متاسفانه درخواست شما با مشکل مواجه شده است",
            s21: "سیاست حفظ اطلاعات و محرمانگی اطلاعات کاربران در پارسو",
            s22: "پارسو معتقد است که اصول حفظ حریم شخصی و اطلاعات کاربران بسیار مهم و حساس است و خود را متعهد به حفاظت از حریم خصوصی تمامی کاربران می‌داند. ما به عنوان بخشی از تعهد خود نسبت به حریم خصوصی کاربران، در این سند اصول محرمانگی اطلاعات کاربران و نیز راهکارهای پارسو برای حفظ حریم شخصی و اطلاعات کاربران را توضیح داده‌ایم.",
            s23: "پارسو نهایت تلاش خود را در راستای حفظ امنیت اطلاعات شخصی کاربران به کار برده و از این اطلاعات در جهت سرویس‌دهی بهتر و پاسخ دهی به موقع و مناسب استفاده می‌کند که در ادامه متن، شرح آن را به اطلاع شما می‌رسانیم.",
            s24: "پارسو، اطلاعاتی را از کاربر دریافت می‌کند که بخشی از این اطلاعات توسط خود کاربر ارائه شده و  بخشی نیز به صورت خودکار توسط سیستم کاربر دریافت می‌گردد که البته شامل اطلاعات شخصی و حریم خصوصی کاربر نمی‌شود.",
            s25: "محتویات این سند",
            s26: "این سند شامل توضیحی درباره اطلاعات خصوصی‌ای است که پارسو از کاربران جمع‌آوری می‌کند و نیز نحوه استفاده پارسو از این اطلاعات است. این اطلاعات شامل مواردی است که مخصوص هر کاربر بوده و به کمک آن‌ها می‌توان کاربران پارسو را به صورت منحصر بفرد شناسایی نمود. برای نمونه می‌توان به نام، نشانی الکترونیک (ایمیل) و سایر اطلاعاتی که به صورت عادی در دسترس عموم مردم نیست اشاره نمود.",
            s27: "این سند به مواردی که در اختیار پارسو نیست و پارسو هیچ کنترلی بر آن‌ها ندارد نمی‌پردازد. شرکت‌های مشتری خدمات پارسو و افراد ثالثی که در استخدام پارسو نیستند و همچنین اطلاعات ثبت شده در Whois  نام دامنه کاربر از جمله این موارد هستند.",
            s28: "درباره ما",
            s29: "این اعلان حریم خصوصی به شما می‌گوید که چه اطلاعاتی هنگام ثبت نام سامانه احراز هویت متمرکز پارسو از شما دریافت و جمع‌آوری می‌شود. پارسو یک سرویس مدیریت و احراز هویت است که اطلاعات هویتی کاربر را ایجاد، نگهداری و مدیریت می‌کند و سرویس‌های احراز هویت را برای برنامه‌های مجاز و اپلیکیشن‌های شخص ثالث ارائه می‌دهد. در جمع‌آوری این اطلاعات ما به عنوان کنترل‌کننده دامنه عمل می‌کنیم و طبق قانون موظفیم اطلاعاتی را در مورد ما، چرایی و نحوه استفاده از داده‌های شما و حقوقی که نسبت به داده‌های خود دارید به شما ارائه دهیم. برای ارتباط و کسب اطلاعات بیشتر به آدرس وب‌سایت ما به نشانی",
            s30: "اطلاعات جمع‌آوری شده",
            s31: "اطلاعات شما توسط ما برای ارائه خدمات در ایجاد یک حساب کاربری SSO مورد استفاده قرار می‌گیرد. سپس از این حساب کاربری جهت دسترسی به برخی خدمات پارسو و سرویس‌های شخص ثالث استفاده می‌شود.",
            s32: "برخی از داده‌ها ، که بر روی سرور ما ذخیره می‎شود، داده‌هایی است که شما، به طور داوطلبانه و از طریق تکمیل فرم ها ارائه داده اید؛ در حالی که سایر داده‌ها به طور خودکار جمع‌آوری می‌شود.",
            s33: "هنگام ورود به پارسو، رویداد‌های حساب کاربری شامل اطلاعاتی همچون دفعات ورود، تاریخ و ساعت ورود،‌ آدرس آی‌پی، نوع سیستم‌عامل و سایر اطلاعات تنها به منظور بهبود خدمات استفاده خواهند شد و ارتباطی با اطلاعات شخصی فرد نخواهد داشت. این اطلاعات نیز به هیچ عنوان به سازمان‌های ثالث فروخته یا ارائه نخواهد شد و تنها به عنوان بهینه‌سازی خدمات پارسو استفاده خواهد شد.",
            s34: "چه مدت اطلاعات شما را نگه می‌داریم؟",
            s35: "ما اطلاعات شما را تا زمانی که شما یک حساب SSO را حفظ کرده اید و پس از آن برای مدت زمانی که منطقی مطابق با خط مشی حفظ سوابق ما لازم باشد، نگه می‌داریم.",
            s36: "حقوق شما نسبت به اطلاعات شما",
            s37: "طبق قانون، می‌توانید از ما بخواهید که چه اطلاعاتی را در مورد شما در دست داریم و می‌توانید از ما بخواهید که اگر نادرست باشد، آن را اصلاح کنیم. می توانید طبق فرایند تعریف شده در توافقنامه¬ای که با سازمان شما داریم نسبت به تغییر اطلاعات خود اقدام نمایید.",
            s38: "همچنین می‌توانید بخواهید که آن را پاک کنید و می‌توانید از ما بخواهید که یک نسخه از اطلاعات را به شما ارائه دهیم. اگر از ما بخواهید حساب SSO خود را حذف کنیم، دیگر نمی‌توانید به برخی خدمات پارسو دسترسی پیدا کنید.",
            s39: "سامانه‌های متصل به پارسو",
            s40: "پس از ورود به پارسو، کاربران می‌توانند به سامانه‌های متصل به آن دسترسی داشته باشند، این سامانه‌ها تحت نظارت پارسو نیستند و پارسو در مورد آنها مسئولیتی ندارد. باید توجه داشته باشید كه اطلاعات شخصی كه برای سامانه‌ها می‌فرستید،‌ تحت نظارت خط مشی حریم خصوصی پارسو نخواهد بود و توصیه می‌كنیم كه نسبت به سیستم حفظ حریم خصوصی و سیاست‌های امنیتی آنها اطلاع حاصل كنید.",
            s41: "کوکی‌ها",
            s42: "هنگام بازدید از وب سایت، ممکن است اطلاعاتی در رایانه شما از طریق کوکی‌ها ذخیره شود که این کوکی‌ها امکان شناسایی خودکار رایانه را در بازدید بعدی از وب‌سایت فراهم می‌سازد. کوکی به خودی خود، شامل اطلاعات شخصی نیست. لیکن در عین حال، ما را قادر می نماید که استفاده شما از وب‌سایت حاضر را با اطلاعاتی که به طور خاص و آگاهانه ارائه کرده‌اید مرتبط نماییم. تنها اطلاعات شخصی که یک کوکی می تواند در برگیرد، اطلاعاتی است که خود شما ارائه نموده اید. کوکی قادر به خواندن داده های موجود روی هارد دیسک و یا فایل های کوکی که توسط وب سایت‌های دیگر ایجاد شده، نیست. شما می‌توانید مرورگر خود را در حالت رد تمامی کوکی ها یا اعلام ارسال کوکی، تنظیم نمایید. در صورتی که کوکی‌ها را نپذیرید، استفاده از بعضی قسمت‌های وب سایت حاضر ممکن است امکان‌پذیر نباشد.",
            s43: "یکی دیگر از کاربرد های کوکی‌ها جلوگیری از اجبار برای وارد کردن مداوم مشخصات ورود در پورتال یا سرویس‌ها می‌باشد. به این ترتیب در صورتی که کاربر یک بار با استفاده از نام کاربری و رمز عبور خود وارد پورتال یا سرویس خود شود پس از آن یک کوکی در سیستم کاربر ذخیره می‌شود سپس تا زمان مشخص شده استاندارد یا خروج از سامانه، کاربر می‌تواند بدون وارد کردن مشخصات ورود از امکانات پارسو استفاده نماید.",
            s44: "امنیت و حفاظت دسترسی و بروزرسانی",
            s45: "ما از اقدامات امنیتی استفاده می‌کنیم تا اطلاعات شما را از دسترسی اشخاص غیر مجاز مصون نگه داريم، شما این حق را دارید که از اطلاعات نگهداری شده خود آگاهی داشته و از ما بخواهید تا تغییراتی در آن‌ها ایجاد کنیم تا دقیق و به روز باشند. اگر به این امر تمایل داشتید، ما را در جریان بگذارید.",
            s46: "تغییرات در خط مشی حفظ حریم خصوصی ما",
            s47: "هرگونه تغییرات آتی در خط مشی حفظ حریم خصوصی ما بر روی سایت قرار خواهد گرفت و در صورت نیاز از طریق ایمیل نیز اطلاع رسانی خواهد شد. تغییرات در سیاست ما به طور منظم در وب‌سایت به روز می‌شوند. در صورت داشتن هرگونه سوال، با ما تماس بگیرید.",
            s48: "پرسش‌ها و پیشنهادها",
            s49: "در صورتی که درباره این سیاست‌ها پرسشی دارید یا پیشنهادهایی برای تغییرات و بهبود آن‌ها دارید، با استفاده از فرم ارتباط با ما آن را با تیم پارسو در میان بگذارید.",
            s50: "مراجعه نمایید."
        },
        created: function () {
            this.setDateNav();
            if(window.localStorage.getItem("lang") === null){
                window.localStorage.setItem("lang", "FA");
            }else if(window.localStorage.getItem("lang") === "EN") {
                this.changeLang();
            }
        },
        methods: {
            setDateNav: function () {
                this.dateNav = new persianDate().format("dddd، DD MMMM YYYY");
                persianDate.toCalendar("gregorian");
                persianDate.toLocale("en");
                this.dateNavEn = new persianDate().format("dddd, DD MMMM YYYY");
                persianDate.toCalendar("persian");
                persianDate.toLocale("fa");
                this.dateNavText = this.dateNav;
            },
            changeLang: function () {
                if (this.lang == "EN") {
                    window.localStorage.setItem("lang", "EN");
                    this.placeholder = "text-align: left;"
                    this.isRtl = false;
                    this.margin = "margin-left: 30px;";
                    this.marg = "margin-right: auto;";
                    this.font = "font-size: 0.9em; text-align: left;"
                    this.lang = "فارسی";
                    this.rtl = "direction: ltr;";
                    this.eye = "left: 3%;";
                    this.dateNavText = this.dateNavEn;
                    this.s0 = "Parsso";
                    this.s1 = "Enter Your Username and Password";
                    this.s2 = "Username";
                    this.s3 = "Password";
                    this.s4 = "Sign in";
                    this.s5 = "Invalid Username and Password";
                    this.s6 = "New Password";
                    this.s7 = "Repeat New Password";
                    this.s8 = "Submit";
                    this.s9 = "Passwords Don't Match";
                    this.s10 = "Enter your email address that register with:";
                    this.s11 = "Send emial";
                    this.s12 = "Reset Password";
                    this.s13 = "Your Password Must Meet All Of The Following Criteria:";
                    this.s14 = "For Resetting Your Password, Enter Your New Password By Following The Tips Below:";
                    this.s15 = "You Don't Have Permission To Access This Page";
                    this.s16 = "Return";
                    this.s18 = ",";
                    this.s19 = "Dear ";
                    this.s20 = "Sorry, There Was a Problem With Your Request";
                    this.s21 = "Privacy Policy and Users Information Privacy in Parsso",
                    this.s22 = "Parsso believes that the principles of privacy and user information are very important and sensitive, and we are committed to protecting the privacy of all users. As part of our commitment to user privacy, in this document we explain the privacy principles of user information as well as Parsso's privacy and user information strategies.",
                    this.s23 = "Parsso makes every effort to ensure the security of users' useral information and uses this information for better service and timely and appropriate response, the text of which is described below.",
                    this.s24 = "Parsso receives information from the user, part of which is provided by the user, and part is automatically received by the user's system, which of course does not include useral information and user privacy.",
                    this.s25 = "The Contents Of This Document",
                    this.s26 = "This document includes an explanation of the private information that Parsso collects from users, as well as how Parsso uses this information. This information includes items that are specific to each user and can be used to uniquely identify Parsso users. Examples include names, e-mail addresses, and other information that is not normally available to the public.",
                    this.s27 = "This document does not deal with cases in which Parsso is not available and has no control over them. Parsso customer service companies and third parties who are not employed by Parsso, as well as the information registered in Whois of the the domain name of the user are among these cases.",
                    this.s28 = "About Us",
                    this.s29 = "This privacy statement tells you what information will be received and collected when you register for the Parsso Centralized Identification System. Parsso is a management and authentication service that creates, maintains and manages the user's identity information and provides authentication services for authorized applications and third-party applications. In compiling this information, we act as a domain controller and are required by law to provide you with information about us, why and how you use your data, and the rights you have to your data. Contact our website ",
                    this.s30 = "Collected Information",
                    this.s31 = "Your information is used by us to provide services in creating an SSO account. This account is then used to access some Parsso services and third-party services.",
                    this.s32 = "Some of the data stored on our server is data that you provide voluntarily and through the completion of forms; While other data is collected automatically.",
                    this.s33 = "When logging in to Parsso, account events including information such as login, date and time of login, ‌ IP address, type of operating system and other information will only be used to improve services and will not be associated with useral information. This information will not be sold or provided to third parties in any way and will only be used to optimize Parsso services.",
                    this.s34 = "How Long Do We Keep Your Information?",
                    this.s35 = "We will keep your information as long as you maintain an SSO account and then for as long as it makes sense to comply with our backup policy.",
                    this.s36 = "Your Rights To Your Information",
                    this.s37 = "By law, you can ask us what information we have about you, and you can ask us to correct it if it is incorrect. You can change your information according to the process defined in the agreement we have with your organization.",
                    this.s38 = "You can also ask us to delete it and you can ask us to provide you with a copy of the information. If you ask us to delete your SSO account, you will no longer be able to access some Parsso services.",
                    this.s39 = "Parsso Connected Services",
                    this.s40 = "After logging in to Parsso, users can access the services connected to it, these services are not under Parsso's supervision and Parsso is not responsible for them. You should note that the useral information you send to the services will not be monitored by the Privacy Policy, and we recommend that you be aware of the their Privacy Policy and security policies.",
                    this.s41 = "Cookies",
                    this.s42 = "When visiting a website, information may be stored on your computer through cookies that allow you to automatically identify your computer on your next visit to the website. The cookie itself does not contain useral information. But at the same time, it enables us to link your use of this website to information that is specifically and consciously provided.The only useral information a cookie can contain is the information you provide. The cookie cannot read the data on the hard disk or cookie files created by other websites. You can set your browser to reject all cookies or send cookies. If you do not accept cookies, it may not be possible to use certain sections of this website.",
                    this.s43 = "Another use of cookies is to prevent the need to constantly enter login details on portals or services. In this way, if the user enters his portal or service once using his username and password, then a cookie is stored in the user's system, then the user can use Parsso features without entering login specifications until the standard time set or leaving the service.",
                    this.s44 = "Access Security And Protection And Updating",
                    this.s45 = "We use security measures to protect your information from the access of unauthorized users, you have the right to be aware of your stored information and ask us to make changes to it to be accurate and up to date. Let us know if you are interested.",
                    this.s46 = "Changes In Our Privacy Policy",
                    this.s47 = "Any future changes to our privacy policy will be posted on the site and will be notified by email if necessary. Changes in our policy are regularly updated on the website. If you have any questions, please contact us.",
                    this.s48 = "Questions And Suggestions",
                    this.s49 = "If you have any questions about these policies or have suggestions for changes and improvements, please contact Parsso team using the Contact Us form.",
                    this.s50 = " for more information."

                }else {
                    window.localStorage.setItem("lang", "FA");
                    this.placeholder = "text-align: right;";
                    this.isRtl = true;
                    this.margin = "margin-right: 30px;";
                    this.marg = "margin-left: auto;";
                    this.font = "font-size: 0.74em; text-align: right;";
                    this.lang = "EN";
                    this.rtl = "direction: rtl;";
                    this.eye = "right: 3%;";
                    this.dateNavText = this.dateNav;
                    this.s0 = "پارسو";
                    this.s1 = "نام کاربری و رمز عبور خود را وارد کنید";
                    this.s2 = "نام کاربری";
                    this.s3 = "رمز عبور";
                    this.s4 = "ورود";
                    this.s5 = "اطلاعات کاربری نادرست است";
                    this.s6 = "رمز عبور جدید";
                    this.s7 = "تکرار رمز عبور جدید";
                    this.s8 = "تایید";
                    this.s9 = "رمز عبور های وارد شده یکسان نمی باشند";
                    this.s10 = ":آدرس ایمیلی که با آن ثبت نام کرده اید را وارد نمایید"
                    this.s11 = "ارسال ایمیل";
                    this.s12 = "بازنشانی رمز عبور";
                    this.s13 = ":رمز عبور شما باید شامل موارد زیر باشد";
                    this.s14 = ":جهت تکمیل فرآیند بازنشانی رمز عبور خود، با رعایت نکات زیر، رمز عبور جدید خود را وارد نمایید";
                    this.s15 = "شما اجازه دسترسی به این صفحه را ندارید";
                    this.s16 = "بازگشت";
                    this.s18 = " عزیز";
                    this.s19 = "،";
                    this.s20 = "متاسفانه درخواست شما با مشکل مواجه شده است";
                    this.s21 = "سیاست حفظ اطلاعات و محرمانگی اطلاعات کاربران در پارسو",
                    this.s22 = "پارسو معتقد است که اصول حفظ حریم شخصی و اطلاعات کاربران بسیار مهم و حساس است و خود را متعهد به حفاظت از حریم خصوصی تمامی کاربران می‌داند. ما به عنوان بخشی از تعهد خود نسبت به حریم خصوصی کاربران، در این سند اصول محرمانگی اطلاعات کاربران و نیز راهکارهای پارسو برای حفظ حریم شخصی و اطلاعات کاربران را توضیح داده‌ایم.",
                    this.s23 = "پارسو نهایت تلاش خود را در راستای حفظ امنیت اطلاعات شخصی کاربران به کار برده و از این اطلاعات در جهت سرویس‌دهی بهتر و پاسخ دهی به موقع و مناسب استفاده می‌کند که در ادامه متن، شرح آن را به اطلاع شما می‌رسانیم.",
                    this.s24 = "پارسو، اطلاعاتی را از کاربر دریافت می‌کند که بخشی از این اطلاعات توسط خود کاربر ارائه شده و  بخشی نیز به صورت خودکار توسط سیستم کاربر دریافت می‌گردد که البته شامل اطلاعات شخصی و حریم خصوصی کاربر نمی‌شود.",
                    this.s25 = "محتویات این سند",
                    this.s26 = "این سند شامل توضیحی درباره اطلاعات خصوصی‌ای است که پارسو از کاربران جمع‌آوری می‌کند و نیز نحوه استفاده پارسو از این اطلاعات است. این اطلاعات شامل مواردی است که مخصوص هر کاربر بوده و به کمک آن‌ها می‌توان کاربران پارسو را به صورت منحصر بفرد شناسایی نمود. برای نمونه می‌توان به نام، نشانی الکترونیک (ایمیل) و سایر اطلاعاتی که به صورت عادی در دسترس عموم مردم نیست اشاره نمود.",
                    this.s27 = "این سند به مواردی که در اختیار پارسو نیست و پارسو هیچ کنترلی بر آن‌ها ندارد نمی‌پردازد. شرکت‌های مشتری خدمات پارسو و افراد ثالثی که در استخدام پارسو نیستند و همچنین اطلاعات ثبت شده در Whois  نام دامنه کاربر از جمله این موارد هستند.",
                    this.s28 = "درباره ما",
                    this.s29 = "این اعلان حریم خصوصی به شما می‌گوید که چه اطلاعاتی هنگام ثبت نام سامانه احراز هویت متمرکز پارسو از شما دریافت و جمع‌آوری می‌شود. پارسو یک سرویس مدیریت و احراز هویت است که اطلاعات هویتی کاربر را ایجاد، نگهداری و مدیریت می‌کند و سرویس‌های احراز هویت را برای برنامه‌های مجاز و اپلیکیشن‌های شخص ثالث ارائه می‌دهد. در جمع‌آوری این اطلاعات ما به عنوان کنترل‌کننده دامنه عمل می‌کنیم و طبق قانون موظفیم اطلاعاتی را در مورد ما، چرایی و نحوه استفاده از داده‌های شما و حقوقی که نسبت به داده‌های خود دارید به شما ارائه دهیم. برای ارتباط و کسب اطلاعات بیشتر به آدرس وب‌سایت ما به نشانی",
                    this.s30 = "اطلاعات جمع‌آوری شده",
                    this.s31 = "اطلاعات شما توسط ما برای ارائه خدمات در ایجاد یک حساب کاربری SSO مورد استفاده قرار می‌گیرد. سپس از این حساب کاربری جهت دسترسی به برخی خدمات پارسو و سرویس‌های شخص ثالث استفاده می‌شود.",
                    this.s32 = "برخی از داده‌ها ، که بر روی سرور ما ذخیره می‎شود، داده‌هایی است که شما، به طور داوطلبانه و از طریق تکمیل فرم ها ارائه داده اید؛ در حالی که سایر داده‌ها به طور خودکار جمع‌آوری می‌شود.",
                    this.s33 = "هنگام ورود به پارسو، رویداد‌های حساب کاربری شامل اطلاعاتی همچون دفعات ورود، تاریخ و ساعت ورود،‌ آدرس آی‌پی، نوع سیستم‌عامل و سایر اطلاعات تنها به منظور بهبود خدمات استفاده خواهند شد و ارتباطی با اطلاعات شخصی فرد نخواهد داشت. این اطلاعات نیز به هیچ عنوان به سازمان‌های ثالث فروخته یا ارائه نخواهد شد و تنها به عنوان بهینه‌سازی خدمات پارسو استفاده خواهد شد.",
                    this.s34 = "چه مدت اطلاعات شما را نگه می‌داریم؟",
                    this.s35 = "ما اطلاعات شما را تا زمانی که شما یک حساب SSO را حفظ کرده اید و پس از آن برای مدت زمانی که منطقی مطابق با خط مشی حفظ سوابق ما لازم باشد، نگه می‌داریم.",
                    this.s36 = "حقوق شما نسبت به اطلاعات شما",
                    this.s37 = "طبق قانون، می‌توانید از ما بخواهید که چه اطلاعاتی را در مورد شما در دست داریم و می‌توانید از ما بخواهید که اگر نادرست باشد، آن را اصلاح کنیم. می توانید طبق فرایند تعریف شده در توافقنامه¬ای که با سازمان شما داریم نسبت به تغییر اطلاعات خود اقدام نمایید.",
                    this.s38 = "همچنین می‌توانید بخواهید که آن را پاک کنید و می‌توانید از ما بخواهید که یک نسخه از اطلاعات را به شما ارائه دهیم. اگر از ما بخواهید حساب SSO خود را حذف کنیم، دیگر نمی‌توانید به برخی خدمات پارسو دسترسی پیدا کنید.",
                    this.s39 = "سامانه‌های متصل به پارسو",
                    this.s40 = "پس از ورود به پارسو، کاربران می‌توانند به سامانه‌های متصل به آن دسترسی داشته باشند، این سامانه‌ها تحت نظارت پارسو نیستند و پارسو در مورد آنها مسئولیتی ندارد. باید توجه داشته باشید كه اطلاعات شخصی كه برای سامانه‌ها می‌فرستید،‌ تحت نظارت خط مشی حریم خصوصی پارسو نخواهد بود و توصیه می‌كنیم كه نسبت به سیستم حفظ حریم خصوصی و سیاست‌های امنیتی آنها اطلاع حاصل كنید.",
                    this.s41 = "کوکی‌ها",
                    this.s42 = "هنگام بازدید از وب سایت، ممکن است اطلاعاتی در رایانه شما از طریق کوکی‌ها ذخیره شود که این کوکی‌ها امکان شناسایی خودکار رایانه را در بازدید بعدی از وب‌سایت فراهم می‌سازد. کوکی به خودی خود، شامل اطلاعات شخصی نیست. لیکن در عین حال، ما را قادر می نماید که استفاده شما از وب‌سایت حاضر را با اطلاعاتی که به طور خاص و آگاهانه ارائه کرده‌اید مرتبط نماییم. تنها اطلاعات شخصی که یک کوکی می تواند در برگیرد، اطلاعاتی است که خود شما ارائه نموده اید. کوکی قادر به خواندن داده های موجود روی هارد دیسک و یا فایل های کوکی که توسط وب سایت‌های دیگر ایجاد شده، نیست. شما می‌توانید مرورگر خود را در حالت رد تمامی کوکی ها یا اعلام ارسال کوکی، تنظیم نمایید. در صورتی که کوکی‌ها را نپذیرید، استفاده از بعضی قسمت‌های وب سایت حاضر ممکن است امکان‌پذیر نباشد.",
                    this.s43 = "یکی دیگر از کاربرد های کوکی‌ها جلوگیری از اجبار برای وارد کردن مداوم مشخصات ورود در پورتال یا سرویس‌ها می‌باشد. به این ترتیب در صورتی که کاربر یک بار با استفاده از نام کاربری و رمز عبور خود وارد پورتال یا سرویس خود شود پس از آن یک کوکی در سیستم کاربر ذخیره می‌شود سپس تا زمان مشخص شده استاندارد یا خروج از سامانه، کاربر می‌تواند بدون وارد کردن مشخصات ورود از امکانات پارسو استفاده نماید.",
                    this.s44 = "امنیت و حفاظت دسترسی و بروزرسانی",
                    this.s45 = "ما از اقدامات امنیتی استفاده می‌کنیم تا اطلاعات شما را از دسترسی اشخاص غیر مجاز مصون نگه داريم، شما این حق را دارید که از اطلاعات نگهداری شده خود آگاهی داشته و از ما بخواهید تا تغییراتی در آن‌ها ایجاد کنیم تا دقیق و به روز باشند. اگر به این امر تمایل داشتید، ما را در جریان بگذارید.",
                    this.s46 = "تغییرات در خط مشی حفظ حریم خصوصی ما",
                    this.s47 = "هرگونه تغییرات آتی در خط مشی حفظ حریم خصوصی ما بر روی سایت قرار خواهد گرفت و در صورت نیاز از طریق ایمیل نیز اطلاع رسانی خواهد شد. تغییرات در سیاست ما به طور منظم در وب‌سایت به روز می‌شوند. در صورت داشتن هرگونه سوال، با ما تماس بگیرید.",
                    this.s48 = "پرسش‌ها و پیشنهادها",
                    this.s49 = "در صورتی که درباره این سیاست‌ها پرسشی دارید یا پیشنهادهایی برای تغییرات و بهبود آن‌ها دارید، با استفاده از فرم ارتباط با ما آن را با تیم پارسو در میان بگذارید.",
                    this.s50 = "مراجعه نمایید."
                }
            }
        }
    });
})