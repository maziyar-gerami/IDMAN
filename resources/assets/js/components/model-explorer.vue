<template>
    <div class="card">
        <div class="card-header bg-light py-2 d-flex justify-content-between align-items-center">
            <div>
                <span v-text="title"></span>
                <span class="ml-2" v-if="loading">
                    <i class="fa fa-refresh fa-spin"></i>
                </span>
            </div>
            <div class="d-flex">
                <div style="width: 250px">
                    <form class="input-group input-group-sm" @submit.prevent="fetch">
                        <input type="text" class="form-control" placeholder="جستجو..." v-model="query">
                        <div class="input-group-append">
                            <button class="btn btn-secondary" type="submit">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
                <slot name="actions"></slot>
            </div>
        </div>
        <div class="list-group list-group-flush">
            <slot name="models" :models="models" v-if="models.length > 0">
                <div class="list-group-item" v-for="model in models">
                    <slot name="model" :model="model"></slot>
                </div>
            </slot>
            <div class="list-group-item list-group-item-warning text-center" v-else>
                <div>
                    <i class="fa fa-signing fa-2x"></i>
                </div>
                <div>لیست <span v-text="subject"></span> خالی است</div>
            </div>
            <div class="list-group-item list-group-item-danger text-center" v-if="error">
                <div>
                    <i class="fa fa-ban fa-2x"></i>
                </div>
                <div v-text="error"></div>
            </div>
        </div>
        <div class="card-footer py-2" v-if="is_there_multiple_pages">
            <b-pagination class="mb-0" :total-rows="total" :per-page="per_page" v-model="page"></b-pagination>
        </div>
    </div>
</template>
<script>
    export default {
        props: {
            title: {
                type: String
            },
            subject: {
                type: String
            },
            api: {
                type: String,
                required: true,
            }
        },
        data() {
            return {
                loading: false,
                error: '',

                page: 1,
                total: 0,
                per_page: 0,

                query: '',

                models: [],
            }
        },
        computed: {
            is_there_multiple_pages() {
                return this.total > this.per_page;
            },
        },
        watch: {
            page() {
                this.fetch()
            }
        },
        methods: {
            fetch() {
                this.error = '';
                this.loading = true;

                axios.get(this.api, {
                    params: {
                        page: this.page,
                        query: this.query
                    }
                }).then(result => {
                    this.loading = false;

                    this.models = result.data.data;

                    this.page = result.data.current_page;
                    this.total = result.data.total;
                    this.per_page = result.data.per_page;
                }).catch(result => {
                    this.loading = false;
                    this.error = result.response.statusText
                })
            },
        },
        mounted() {
            this.fetch();
        }
    }
</script>