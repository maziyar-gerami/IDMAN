import { createRouter, createWebHashHistory } from "vue-router"
import Dashboard from "../views/Dashboard.vue"

const routes = [
  {
    path: "/",
    name: "Dashboard",
    component: Dashboard,
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/accessreports",
    name: "AccessReports",
    component: () => import("../views/AccessReports.vue"),
    meta: { requiresAccessLevel: 2 }
  },
  {
    path: "/audits",
    name: "Audits",
    component: () => import("../views/Audits.vue"),
    meta: { requiresAccessLevel: 1 }
  },
  {
    path: "/changepassword",
    name: "ChangePassword",
    component: () => import("../views/ChangePassword.vue"),
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/events",
    name: "Events",
    component: () => import("../views/Events.vue"),
    meta: { requiresAccessLevel: 1 }
  },
  {
    path: "/groups",
    name: "Groups",
    component: () => import("../views/Groups.vue"),
    meta: { requiresAccessLevel: 2 }
  },
  {
    path: "/notifications",
    name: "Notifications",
    component: () => import("../views/Notifications.vue"),
    meta: { requiresAccessLevel: 2 }
  },
  {
    path: "/privacy",
    name: "Privacy",
    component: () => import("../views/Privacy.vue"),
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/profile",
    name: "Profile",
    component: () => import("../views/Profile.vue"),
    meta: { requiresAccessLevel: 1 }
  },
  {
    path: "/reports",
    name: "Reports",
    component: () => import("../views/Reports.vue"),
    meta: { requiresAccessLevel: 1 }
  },
  {
    path: "/resetpassword",
    name: "ResetPassword",
    component: () => import("../views/ResetPassword.vue"),
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/roles",
    name: "Roles",
    component: () => import("../views/Roles.vue"),
    meta: { requiresAccessLevel: 4 }
  },
  {
    path: "/services",
    name: "Services",
    component: () => import("../views/Services.vue"),
    meta: { requiresAccessLevel: 2 }
  },
  {
    path: "/settings",
    name: "Settings",
    component: () => import("../views/Settings.vue"),
    meta: { requiresAccessLevel: 4 }
  },
  {
    path: "/ticketing",
    name: "Ticketing",
    component: () => import("../views/Ticketing.vue"),
    meta: { requiresAccessLevel: 1 }
  },
  {
    path: "/users",
    name: "Users",
    component: () => import("../views/Users.vue"),
    meta: { requiresAccessLevel: 2 }
  },
  {
    path: "/Parsso-User-Guide.pdf",
    name: "Parsso-User-Guide.pdf",
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/403",
    name: "Error403",
    component: () => import("../views/Error403.vue"),
    meta: { requiresAccessLevel: 0 }
  },
  {
    path: "/:catchAll(.*)",
    name: "Error404",
    component: () => import("../views/Error404.vue"),
    meta: { requiresAccessLevel: 0 }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
