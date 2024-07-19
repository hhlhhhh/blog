import { createApp } from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './route'

import ArcoVue from '@arco-design/web-vue'

//arco design图标库
import ArcoVueIcon from '@arco-design/web-vue/es/icon'
import '@arco-design/web-vue/dist/arco.css'

//markdown
// highlightjs
import hljs from 'highlight.js';

// codemirror 编辑器的相关资源
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import Codemirror from 'codemirror';
// mode
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edit
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
// style
import 'codemirror/lib/codemirror.css';

VMdEditor.Codemirror = Codemirror;
VMdEditor.use(githubTheme, {
  Hljs: hljs,
});
VMdPreview.use(githubTheme, {
    Hljs: hljs,
});

const app = createApp(App)

const pinia = createPinia()
app.use(VMdEditor);
app.use(VMdPreview);
app.use(router)
app.use(pinia)
app.use(ArcoVueIcon)
app.use(ArcoVue)

app.mount('#app')
