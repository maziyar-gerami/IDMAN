<template>
    <div>
        <form class="input-group" @submit.prevent="fetch">
            <input type="text" class="form-control" placeholder="جستجو..." v-model="query">
            <div class="input-group-append">
                <button class="btn btn-secondary" type="submit">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </form>

        <div class="mt-3 alert alert-danger text-center" v-if="error">
            <div>
                <i class="fa fa-ban fa-2x"></i>
            </div>
            <div v-text="error"></div>
        </div>

        <div class="list-unstyled mt-3 mb-0" v-if="models.length > 0">
            <a href="javascript:void(0)" class="p-2 media" style="text-decoration: none"
               :class="{'bg-primary text-white': selected == model.id}" @click="select(model)"
               v-for="model in models">
                <img :src="model.avatar_url" alt="" class="mr-3 border" height="50">
                <div class="media-body">
                    <div>
                        <strong v-text="model.username"></strong>
                    </div>
                    <div>
                        <small>
                            <span v-text="model.first_name || ''"></span>
                            <span v-text="model.last_name || ''"></span>
                        </small>
                    </div>
                </div>
            </a>
        </div>

        <div class="mt-3" v-if="is_there_multiple_pages">
            <b-pagination class="mb-0" :total-rows="total" :per-page="per_page" v-model="page"></b-pagination>
        </div>
    </div>
</template>
<script>
    export default {
        props: {
            api: {
                type: String,
                required: true,
            },
        },
        data() {
            return {
                selected: null,
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
            select(user) {
                this.selected = user.id;
                this.$emit('selected', user);
            }
        },
        mounted() {
            this.fetch();
        }
    }
</script>