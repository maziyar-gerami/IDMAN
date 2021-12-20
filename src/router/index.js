import { createRouter, createWebHistory } from "vue-router"
import AccessReports from "../views/AccessReports.vue"
import Audits from "../views/Audits.vue"
import ChangePassword from "../views/ChangePassword.vue"
import Dashboard from "../views/Dashboard.vue"
import Error from "../views/Error.vue"
import Events from "../views/Events.vue"
import Groups from "../views/Groups.vue"
import Notifications from "../views/Notifications.vue"
import Profile from "../views/Profile.vue"
import Reports from "../views/Reports.vue"
import ResetPassword from "../views/ResetPassword.vue"
import Roles from "../views/Roles.vue"
import Services from "../views/Services.vue"
import Settings from "../views/Settings.vue"
import Ticketing from "../views/Ticketing.vue"
import Users from "../views/Users.vue"

const routes = [
  {
    path: "/accessreports",
    name: "AccessReports",
    component: AccessReports
  },
  {
    path: "/audits",
    name: "Audits",
    component: Audits
  },
  {
    path: "/changepassword",
    name: "ChangePassword",
    component: ChangePassword
  },
  {
    path: "/",
    name: "Dashboard",
    component: Dashboard
  },
  {
    path: "/error",
    name: "Error",
    component: Error
  },
  {
    path: "/events",
    name: "Events",
    component: Events
  },
  {
    path: "/groups",
    name: "Groups",
    component: Groups
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: Notifications
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile
  },
  {
    path: "/reports",
    name: "Reports",
    component: Reports
  },
  {
    path: "/resetpassword",
    name: "ResetPassword",
    component: ResetPassword
  },
  {
    path: "/roles",
    name: "Roles",
    component: Roles
  },
  {
    path: "/services",
    name: "Services",
    component: Services
  },
  {
    path: "/settings",
    name: "Settings",
    component: Settings
  },
  {
    path: "/ticketing",
    name: "Ticketing",
    component: Ticketing
  },
  {
    path: "/users",
    name: "Users",
    component: Users
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
