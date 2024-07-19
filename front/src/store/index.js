
import { defineStore } from 'pinia'

export const ChannelStore = defineStore('Channel', { 
    state: () => {
        return {
            navChannelList: [],
        }
    },
    getters: {
        getNavChannelList: (state) => {
            return state.navChannelList
        }
    },
    actions: {
        setNavChannelList(arg){
            this.navChannelList = arg
        }
    }
})

export const ArticleStore = defineStore('Article', { 
    state: () => {
        return {
            topArticle: [],
            articleList: [],
            RankarticleList: [],
            articlePageView:{
                size:5,
                current:1,
                keyword:"",
                field:""
            },
            searchFlag:false,    //是否进行搜索
            noMore:false,       //是否没有更多数据
            tagSearchFlag:false //用于触发tag搜索
        }
    },
    getters: {
        getTopArticle: (state) => {
            return state.topArticle
        },
        getArticleList: (state) => {
            return state.articleList
        },
        getRankarticleList: (state) => {
            return state.RankarticleList
        },
        getArticlePageView: (state) => {
            return state.articlePageView
        },
        getSearchFlag: (state) => {
            return state.searchFlag
        },
        getNoMore: (state) => {
            return state.noMore
        },
        getTagSearchFlag: (state) => {
            return state.tagSearchFlag
        }
    },
    actions: {
        setArticlePageView(arg){
            this.articlePageView = arg
        },
        setSearchFlag(arg){
            this.searchFlag = arg
        },
        setTopArticle(arg){
            this.topArticle.length = 0
            this.topArticle.push(...arg)
        },
        setArticleList(arg){
            this.articleList.length = 0
            this.articleList.push(...arg)
        },
        addArticleList(arg){
            this.articleList.push(...arg)
        },
        setRankarticleList(arg){
            this.RankarticleList = arg
        },
        setNoMore(arg){
            this.noMore = arg
        },
        setTagSearchFlag(arg){
            this.tagSearchFlag = arg
        }
    }
})

export const UserStore = defineStore('User', { 
    state: () => {
        return {
            user: {
                id:353,
                account:"xiaoshun",
                password:"123456",
                nickname:"赵国顺",
                email:"findshun@qq.com",
                avatar:"https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp",
                createTime:new Date(),
                status: 1,
                profile: "我是一条自由的鱼~"
            },
            token: null
        }
    },
    getters: {
        getUser: (state) => {
            return state.user
        },
        getToken: (state) => {
            return state.token
        }
    },
    actions: {
        setUser(arg){
            this.user = arg
        },
        setToken(arg){
            this.token = arg
        }
    }
})