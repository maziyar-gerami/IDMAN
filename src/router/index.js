import { createRouter, createWebHistory } from "vue-router"
import Dashboard from "../views/Dashboard.vue"

const routes = [
  {
    path: "/",
    name: "Dashboard",
    component: Dashboard
  },
  {
    path: "/accessreports",
    name: "AccessReports",
    component: () => import("../views/AccessReports.vue")
  },
  {
    path: "/audits",
    name: "Audits",
    component: () => import("../views/Audits.vue")
  },
  {
    path: "/changepassword",
    name: "ChangePassword",
    component: () => import("../views/ChangePassword.vue")
  },
  {
    path: "/error",
    name: "Error",
    component: () => import("../views/Error.vue")
  },
  {
    path: "/events",
    name: "Events",
    component: () => import("../views/Events.vue")
  },
  {
    path: "/groups",
    name: "Groups",
    component: () => import("../views/Groups.vue")
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: () => import("../views/Notifications.vue")
  },
  {
    path: "/profile",
    name: "Profile",
    component: () => import("../views/Profile.vue")
  },
  {
    path: "/reports",
    name: "Reports",
    component: () => import("../views/Reports.vue")
  },
  {
    path: "/resetpassword",
    name: "ResetPassword",
    component: () => import("../views/ResetPassword.vue")
  },
  {
    path: "/roles",
    name: "Roles",
    component: () => import("../views/Roles.vue")
  },
  {
    path: "/services",
    name: "Services",
    component: () => import("../views/Services.vue")
  },
  {
    path: "/settings",
    name: "Settings",
    component: () => import("../views/Settings.vue")
  },
  {
    path: "/ticketing",
    name: "Ticketing",
    component: () => import("../views/Ticketing.vue")
  },
  {
    path: "/users",
    name: "Users",
    component: () => import("../views/Users.vue")
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
