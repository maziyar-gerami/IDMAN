/**
 * First we will load all of this project's JavaScript dependencies which
 * includes Vue and other libraries. It is a great starting point when
 * building robust, powerful web applications using Vue and Laravel.
 */

require('./bootstrap');

window.Vue = require('vue');
window.XLSX = require('xlsx');

/**
 * Next, we will create a fresh Vue application instance and attach it to
 * the page. Then, you may begin adding components to this application
 * or customize the JavaScript scaffolding to fit your unique needs.
 */
import VueFormWizard from 'vue-form-wizard'
import VueModelExplorer from './components/model-explorer'
import VueSelectorSearch from './components/user-selector-search';
import VueParameterEditor from './components/parameter-editor';
import bPagination from 'bootstrap-vue/es/components/pagination/pagination';

Vue.use(VueFormWizard);
Vue.component('model-explorer', VueModelExplorer);
Vue.component('user-selector-search', VueSelectorSearch);
Vue.component('parameter-editor', VueParameterEditor);
Vue.component('b-pagination', bPagination);

require('../vendor/vue-forms/js/vue-forms');

window.selectMember = require('./user-selector');
window.editParams = require('./parameter-editor');