/*! jQuery v1.9.1 | (c) 2005, 2012 jQuery Foundation, Inc. | jquery.org/license
//@ sourceMappingURL=jquery.min.map
*/(function(e,t){var n,r,i=typeof t,o=e.document,a=e.location,s=e.jQuery,u=e.$,l={},c=[],p="1.9.1",f=c.concat,d=c.push,h=c.slice,g=c.indexOf,m=l.toString,y=l.hasOwnProperty,v=p.trim,b=function(e,t){return new b.fn.init(e,t,r)},x=/[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,w=/\S+/g,T=/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,N=/^(?:(<[\w\W]+>)[^>]*|#([\w-]*))$/,C=/^<(\w+)\s*\/?>(?:<\/\1>|)$/,k=/^[\],:{}\s]*$/,E=/(?:^|:|,)(?:\s*\[)+/g,S=/\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,A=/"[^"\\\r\n]*"|true|false|null|-?(?:\d+\.|)\d+(?:[eE][+-]?\d+|)/g,j=/^-ms-/,D=/-([\da-z])/gi,L=function(e,t){return t.toUpperCase()},H=function(e){(o.addEventListener||"load"===e.type||"complete"===o.readyState)&&(q(),b.ready())},q=function(){o.addEventListener?(o.removeEventListener("DOMContentLoaded",H,!1),e.removeEventListener("load",H,!1)):(o.detachEvent("onreadystatechange",H),e.detachEvent("onload",H))};b.fn=b.prototype={jquery:p,constructor:b,init:function(e,n,r){var i,a;if(!e)return this;if("string"==typeof e){if(i="<"===e.charAt(0)&&">"===e.charAt(e.length-1)&&e.length>=3?[null,e,null]:N.exec(e),!i||!i[1]&&n)return!n||n.jquery?(n||r).find(e):this.constructor(n).find(e);if(i[1]){if(n=n instanceof b?n[0]:n,b.merge(this,b.parseHTML(i[1],n&&n.nodeType?n.ownerDocument||n:o,!0)),C.test(i[1])&&b.isPlainObject(n))for(i in n)b.isFunction(this[i])?this[i](n[i]):this.attr(i,n[i]);return this}if(a=o.getElementById(i[2]),a&&a.parentNode){if(a.id!==i[2])return r.find(e);this.length=1,this[0]=a}return this.context=o,this.selector=e,this}return e.nodeType?(this.context=this[0]=e,this.length=1,this):b.isFunction(e)?r.ready(e):(e.selector!==t&&(this.selector=e.selector,this.context=e.context),b.makeArray(e,this))},selector:"",length:0,size:function(){return this.length},toArray:function(){return h.call(this)},get:function(e){return null==e?this.toArray():0>e?this[this.length+e]:this[e]},pushStack:function(e){var t=b.merge(this.constructor(),e);return t.prevObject=this,t.context=this.context,t},each:function(e,t){return b.each(this,e,t)},ready:function(e){return b.ready.promise().done(e),this},slice:function(){return this.pushStack(h.apply(this,arguments))},first:function(){return this.eq(0)},last:function(){return this.eq(-1)},eq:function(e){var t=this.length,n=+e+(0>e?t:0);return this.pushStack(n>=0&&t>n?[this[n]]:[])},map:function(e){return this.pushStack(b.map(this,function(t,n){return e.call(t,n,t)}))},end:function(){return this.prevObject||this.constructor(null)},push:d,sort:[].sort,splice:[].splice},b.fn.init.prototype=b.fn,b.extend=b.fn.extend=function(){var e,n,r,i,o,a,s=arguments[0]||{},u=1,l=arguments.length,c=!1;for("boolean"==typeof s&&(c=s,s=arguments[1]||{},u=2),"object"==typeof s||b.isFunction(s)||(s={}),l===u&&(s=this,--u);l>u;u++)if(null!=(o=arguments[u]))for(i in o)e=s[i],r=o[i],s!==r&&(c&&r&&(b.isPlainObject(r)||(n=b.isArray(r)))?(n?(n=!1,a=e&&b.isArray(e)?e:[]):a=e&&b.isPlainObject(e)?e:{},s[i]=b.extend(c,a,r)):r!==t&&(s[i]=r));return s},b.extend({noConflict:function(t){return e.$===b&&(e.$=u),t&&e.jQuery===b&&(e.jQuery=s),b},isReady:!1,readyWait:1,holdReady:function(e){e?b.readyWait++:b.ready(!0)},ready:function(e){if(e===!0?!--b.readyWait:!b.isReady){if(!o.body)return setTimeout(b.ready);b.isReady=!0,e!==!0&&--b.readyWait>0||(n.resolveWith(o,[b]),b.fn.trigger&&b(o).trigger("ready").off("ready"))}},isFunction:function(e){return"function"===b.type(e)},isArray:Array.isArray||function(e){return"array"===b.type(e)},isWindow:function(e){return null!=e&&e==e.window},isNumeric:function(e){return!isNaN(parseFloat(e))&&isFinite(e)},type:function(e){return null==e?e+"":"object"==typeof e||"function"==typeof e?l[m.call(e)]||"object":typeof e},isPlainObject:function(e){if(!e||"object"!==b.type(e)||e.nodeType||b.isWindow(e))return!1;try{if(e.constructor&&!y.call(e,"constructor")&&!y.call(e.constructor.prototype,"isPrototypeOf"))return!1}catch(n){return!1}var r;for(r in e);return r===t||y.call(e,r)},isEmptyObject:function(e){var t;for(t in e)return!1;return!0},error:function(e){throw Error(e)},parseHTML:function(e,t,n){if(!e||"string"!=typeof e)return null;"boolean"==typeof t&&(n=t,t=!1),t=t||o;var r=C.exec(e),i=!n&&[];return r?[t.createElement(r[1])]:(r=b.buildFragment([e],t,i),i&&b(i).remove(),b.merge([],r.childNodes))},parseJSON:function(n){return e.JSON&&e.JSON.parse?e.JSON.parse(n):null===n?n:"string"==typeof n&&(n=b.trim(n),n&&k.test(n.replace(S,"@").replace(A,"]").replace(E,"")))?Function("return "+n)():(b.error("Invalid JSON: "+n),t)},parseXML:function(n){var r,i;if(!n||"string"!=typeof n)return null;try{e.DOMParser?(i=new DOMParser,r=i.parseFromString(n,"text/xml")):(r=new ActiveXObject("Microsoft.XMLDOM"),r.async="false",r.loadXML(n))}catch(o){r=t}return r&&r.documentElement&&!r.getElementsByTagName("parsererror").length||b.error("Invalid XML: "+n),r},noop:function(){},globalEval:function(t){t&&b.trim(t)&&(e.execScript||function(t){e.eval.call(e,t)})(t)},camelCase:function(e){return e.replace(j,"ms-").replace(D,L)},nodeName:function(e,t){return e.nodeName&&e.nodeName.toLowerCase()===t.toLowerCase()},each:function(e,t,n){var r,i=0,o=e.length,a=M(e);if(n){if(a){for(;o>i;i++)if(r=t.apply(e[i],n),r===!1)break}else for(i in e)if(r=t.apply(e[i],n),r===!1)break}else if(a){for(;o>i;i++)if(r=t.call(e[i],i,e[i]),r===!1)break}else for(i in e)if(r=t.call(e[i],i,e[i]),r===!1)break;return e},trim:v&&!v.call("\ufeff\u00a0")?function(e){return null==e?"":v.call(e)}:function(e){return null==e?"":(e+"").replace(T,"")},makeArray:function(e,t){var n=t||[];return null!=e&&(M(Object(e))?b.merge(n,"string"==typeof e?[e]:e):d.call(n,e)),n},inArray:function(e,t,n){var r;if(t){if(g)return g.call(t,e,n);for(r=t.length,n=n?0>n?Math.max(0,r+n):n:0;r>n;n++)if(n in t&&t[n]===e)return n}return-1},merge:function(e,n){var r=n.length,i=e.length,o=0;if("number"==typeof r)for(;r>o;o++)e[i++]=n[o];else while(n[o]!==t)e[i++]=n[o++];return e.length=i,e},grep:function(e,t,n){var r,i=[],o=0,a=e.length;for(n=!!n;a>o;o++)r=!!t(e[o],o),n!==r&&i.push(e[o]);return i},map:function(e,t,n){var r,i=0,o=e.length,a=M(e),s=[];if(a)for(;o>i;i++)r=t(e[i],i,n),null!=r&&(s[s.length]=r);else for(i in e)r=t(e[i],i,n),null!=r&&(s[s.length]=r);return f.apply([],s)},guid:1,proxy:function(e,n){var r,i,o;return"string"==typeof n&&(o=e[n],n=e,e=o),b.isFunction(e)?(r=h.call(arguments,2),i=function(){return e.apply(n||this,r.concat(h.call(arguments)))},i.guid=e.guid=e.guid||b.guid++,i):t},access:function(e,n,r,i,o,a,s){var u=0,l=e.length,c=null==r;if("object"===b.type(r)){o=!0;for(u in r)b.access(e,n,u,r[u],!0,a,s)}else if(i!==t&&(o=!0,b.isFunction(i)||(s=!0),c&&(s?(n.call(e,i),n=null):(c=n,n=function(e,t,n){return c.call(b(e),n)})),n))for(;l>u;u++)n(e[u],r,s?i:i.call(e[u],u,n(e[u],r)));return o?e:c?n.call(e):l?n(e[0],r):a},now:function(){return(new Date).getTime()}}),b.ready.promise=function(t){if(!n)if(n=b.Deferred(),"complete"===o.readyState)setTimeout(b.ready);else if(o.addEventListener)o.addEventListener("DOMContentLoaded",H,!1),e.addEventListener("load",H,!1);else{o.attachEvent("onreadystatechange",H),e.attachEvent("onload",H);var r=!1;try{r=null==e.frameElement&&o.documentElement}catch(i){}r&&r.doScroll&&function a(){if(!b.isReady){try{r.doScroll("left")}catch(e){return setTimeout(a,50)}q(),b.ready()}}()}return n.promise(t)},b.each("Boolean Number String Function Array Date RegExp Object Error".split(" "),function(e,t){l["[object "+t+"]"]=t.toLowerCase()});function M(e){var t=e.length,n=b.type(e);return b.isWindow(e)?!1:1===e.nodeType&&t?!0:"array"===n||"function"!==n&&(0===t||"number"==typeof t&&t>0&&t-1 in e)}r=b(o);var _={};function F(e){var t=_[e]={};return b.each(e.match(w)||[],function(e,n){t[n]=!0}),t}b.Callbacks=function(e){e="string"==typeof e?_[e]||F(e):b.extend({},e);var n,r,i,o,a,s,u=[],l=!e.once&&[],c=function(t){for(r=e.memory&&t,i=!0,a=s||0,s=0,o=u.length,n=!0;u&&o>a;a++)if(u[a].apply(t[0],t[1])===!1&&e.stopOnFalse){r=!1;break}n=!1,u&&(l?l.length&&c(l.shift()):r?u=[]:p.disable())},p={add:function(){if(u){var t=u.length;(function i(t){b.each(t,function(t,n){var r=b.type(n);"function"===r?e.unique&&p.has(n)||u.push(n):n&&n.length&&"string"!==r&&i(n)})})(arguments),n?o=u.length:r&&(s=t,c(r))}return this},remove:function(){return u&&b.each(arguments,function(e,t){var r;while((r=b.inArray(t,u,r))>-1)u.splice(r,1),n&&(o>=r&&o--,a>=r&&a--)}),this},has:function(e){return e?b.inArray(e,u)>-1:!(!u||!u.length)},empty:function(){return u=[],this},disable:function(){return u=l=r=t,this},disabled:function(){return!u},lock:function(){return l=t,r||p.disable(),this},locked:function(){return!l},fireWith:function(e,t){return t=t||[],t=[e,t.slice?t.slice():t],!u||i&&!l||(n?l.push(t):c(t)),this},fire:function(){return p.fireWith(this,arguments),this},fired:function(){return!!i}};return p},b.extend({Deferred:function(e){var t=[["resolve","done",b.Callbacks("once memory"),"resolved"],["reject","fail",b.Callbacks("once memory"),"rejected"],["notify","progress",b.Callbacks("memory")]],n="pending",r={state:function(){return n},always:function(){return i.done(arguments).fail(arguments),this},then:function(){var e=arguments;return b.Deferred(function(n){b.each(t,function(t,o){var a=o[0],s=b.isFunction(e[t])&&e[t];i[o[1]](function(){var e=s&&s.apply(this,arguments);e&&b.isFunction(e.promise)?e.promise().done(n.resolve).fail(n.reject).progress(n.notify):n[a+"With"](this===r?n.promise():this,s?[e]:arguments)})}),e=null}).promise()},promise:function(e){return null!=e?b.extend(e,r):r}},i={};return r.pipe=r.then,b.each(t,function(e,o){var a=o[2],s=o[3];r[o[1]]=a.add,s&&a.add(function(){n=s},t[1^e][2].disable,t[2][2].lock),i[o[0]]=function(){return i[o[0]+"With"](this===i?r:this,arguments),this},i[o[0]+"With"]=a.fireWith}),r.promise(i),e&&e.call(i,i),i},when:function(e){var t=0,n=h.call(arguments),r=n.length,i=1!==r||e&&b.isFunction(e.promise)?r:0,o=1===i?e:b.Deferred(),a=function(e,t,n){return function(r){t[e]=this,n[e]=arguments.length>1?h.call(arguments):r,n===s?o.notifyWith(t,n):--i||o.resolveWith(t,n)}},s,u,l;if(r>1)for(s=Array(r),u=Array(r),l=Array(r);r>t;t++)n[t]&&b.isFunction(n[t].promise)?n[t].promise().done(a(t,l,n)).fail(o.reject).progress(a(t,u,s)):--i;return i||o.resolveWith(l,n),o.promise()}}),b.support=function(){var t,n,r,a,s,u,l,c,p,f,d=o.createElement("div");if(d.setAttribute("className","t"),d.innerHTML="  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>",n=d.getElementsByTagName("*"),r=d.getElementsByTagName("a")[0],!n||!r||!n.length)return{};s=o.createElement("select"),l=s.appendChild(o.createElement("option")),a=d.getElementsByTagName("input")[0],r.style.cssText="top:1px;float:left;opacity:.5",t={getSetAttribute:"t"!==d.className,leadingWhitespace:3===d.firstChild.nodeType,tbody:!d.getElementsByTagName("tbody").length,htmlSerialize:!!d.getElementsByTagName("link").length,style:/top/.test(r.getAttribute("style")),hrefNormalized:"/a"===r.getAttribute("href"),opacity:/^0.5/.test(r.style.opacity),cssFloat:!!r.style.cssFloat,checkOn:!!a.value,optSelected:l.selected,enctype:!!o.createElement("form").enctype,html5Clone:"<:nav></:nav>"!==o.createElement("nav").cloneNode(!0).outerHTML,boxModel:"CSS1Compat"===o.compatMode,deleteExpando:!0,noCloneEvent:!0,inlineBlockNeedsLayout:!1,shrinkWrapBlocks:!1,reliableMarginRight:!0,boxSizingReliable:!0,pixelPosition:!1},a.checked=!0,t.noCloneChecked=a.cloneNode(!0).checked,s.disabled=!0,t.optDisabled=!l.disabled;try{delete d.test}catch(h){t.deleteExpando=!1}a=o.createElement("input"),a.setAttribute("value",""),t.input=""===a.getAttribute("value"),a.value="t",a.setAttribute("type","radio"),t.radioValue="t"===a.value,a.setAttribute("checked","t"),a.setAttribute("name","t"),u=o.createDocumentFragment(),u.appendChild(a),t.appendChecked=a.checked,t.checkClone=u.cloneNode(!0).cloneNode(!0).lastChild.checked,d.attachEvent&&(d.attachEvent("onclick",function(){t.noCloneEvent=!1}),d.cloneNode(!0).click());for(f in{submit:!0,change:!0,focusin:!0})d.setAttribute(c="on"+f,"t"),t[f+"Bubbles"]=c in e||d.attributes[c].expando===!1;return d.style.backgroundClip="content-box",d.cloneNode(!0).style.backgroundClip="",t.clearCloneStyle="content-box"===d.style.backgroundClip,b(function(){var n,r,a,s="padding:0;margin:0;border:0;display:block;box-sizing:content-box;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;",u=o.getElementsByTagName("body")[0];u&&(n=o.createElement("div"),n.style.cssText="border:0;width:0;height:0;position:absolute;top:0;left:-9999px;margin-top:1px",u.appendChild(n).appendChild(d),d.innerHTML="<table><tr><td></td><td>t</td></tr></table>",a=d.getElementsByTagName("td"),a[0].style.cssText="padding:0;margin:0;border:0;display:none",p=0===a[0].offsetHeight,a[0].style.display="",a[1].style.display="none",t.reliableHiddenOffsets=p&&0===a[0].offsetHeight,d.innerHTML="",d.style.cssText="box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;padding:1px;border:1px;display:block;width:4px;margin-top:1%;position:absolute;top:1%;",t.boxSizing=4===d.offsetWidth,t.doesNotIncludeMarginInBodyOffset=1!==u.offsetTop,e.getComputedStyle&&(t.pixelPosition="1%"!==(e.getComputedStyle(d,null)||{}).top,t.boxSizingReliable="4px"===(e.getComputedStyle(d,null)||{width:"4px"}).width,r=d.appendChild(o.createElement("div")),r.style.cssText=d.style.cssText=s,r.style.marginRight=r.style.width="0",d.style.width="1px",t.reliableMarginRight=!parseFloat((e.getComputedStyle(r,null)||{}).marginRight)),typeof d.style.zoom!==i&&(d.innerHTML="",d.style.cssText=s+"width:1px;padding:1px;display:inline;zoom:1",t.inlineBlockNeedsLayout=3===d.offsetWidth,d.style.display="block",d.innerHTML="<div></div>",d.firstChild.style.width="5px",t.shrinkWrapBlocks=3!==d.offsetWidth,t.inlineBlockNeedsLayout&&(u.style.zoom=1)),u.removeChild(n),n=d=a=r=null)}),n=s=u=l=r=a=null,t}();var O=/(?:\{[\s\S]*\}|\[[\s\S]*\])$/,B=/([A-Z])/g;function P(e,n,r,i){if(b.acceptData(e)){var o,a,s=b.expando,u="string"==typeof n,l=e.nodeType,p=l?b.cache:e,f=l?e[s]:e[s]&&s;if(f&&p[f]&&(i||p[f].data)||!u||r!==t)return f||(l?e[s]=f=c.pop()||b.guid++:f=s),p[f]||(p[f]={},l||(p[f].toJSON=b.noop)),("object"==typeof n||"function"==typeof n)&&(i?p[f]=b.extend(p[f],n):p[f].data=b.extend(p[f].data,n)),o=p[f],i||(o.data||(o.data={}),o=o.data),r!==t&&(o[b.camelCase(n)]=r),u?(a=o[n],null==a&&(a=o[b.camelCase(n)])):a=o,a}}function R(e,t,n){if(b.acceptData(e)){var r,i,o,a=e.nodeType,s=a?b.cache:e,u=a?e[b.expando]:b.expando;if(s[u]){if(t&&(o=n?s[u]:s[u].data)){b.isArray(t)?t=t.concat(b.map(t,b.camelCase)):t in o?t=[t]:(t=b.camelCase(t),t=t in o?[t]:t.split(" "));for(r=0,i=t.length;i>r;r++)delete o[t[r]];if(!(n?$:b.isEmptyObject)(o))return}(n||(delete s[u].data,$(s[u])))&&(a?b.cleanData([e],!0):b.support.deleteExpando||s!=s.window?delete s[u]:s[u]=null)}}}b.extend({cache:{},expando:"jQuery"+(p+Math.random()).replace(/\D/g,""),noData:{embed:!0,object:"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",applet:!0},hasData:function(e){return e=e.nodeType?b.cache[e[b.expando]]:e[b.expando],!!e&&!$(e)},data:function(e,t,n){return P(e,t,n)},removeData:function(e,t){return R(e,t)},_data:function(e,t,n){return P(e,t,n,!0)},_removeData:function(e,t){return R(e,t,!0)},acceptData:function(e){if(e.nodeType&&1!==e.nodeType&&9!==e.nodeType)return!1;var t=e.nodeName&&b.noData[e.nodeName.toLowerCase()];return!t||t!==!0&&e.getAttribute("classid")===t}}),b.fn.extend({data:function(e,n){var r,i,o=this[0],a=0,s=null;if(e===t){if(this.length&&(s=b.data(o),1===o.nodeType&&!b._data(o,"parsedAttrs"))){for(r=o.attributes;r.length>a;a++)i=r[a].name,i.indexOf("data-")||(i=b.camelCase(i.slice(5)),W(o,i,s[i]));b._data(o,"parsedAttrs",!0)}return s}return"object"==typeof e?this.each(function(){b.data(this,e)}):b.access(this,function(n){return n===t?o?W(o,e,b.data(o,e)):null:(this.each(function(){b.data(this,e,n)}),t)},null,n,arguments.length>1,null,!0)},removeData:function(e){return this.each(function(){b.removeData(this,e)})}});function W(e,n,r){if(r===t&&1===e.nodeType){var i="data-"+n.replace(B,"-$1").toLowerCase();if(r=e.getAttribute(i),"string"==typeof r){try{r="true"===r?!0:"false"===r?!1:"null"===r?null:+r+""===r?+r:O.test(r)?b.parseJSON(r):r}catch(o){}b.data(e,n,r)}else r=t}return r}function $(e){var t;for(t in e)if(("data"!==t||!b.isEmptyObject(e[t]))&&"toJSON"!==t)return!1;return!0}b.extend({queue:function(e,n,r){var i;return e?(n=(n||"fx")+"queue",i=b._data(e,n),r&&(!i||b.isArray(r)?i=b._data(e,n,b.makeArray(r)):i.push(r)),i||[]):t},dequeue:function(e,t){t=t||"fx";var n=b.queue(e,t),r=n.length,i=n.shift(),o=b._queueHooks(e,t),a=function(){b.dequeue(e,t)};"inprogress"===i&&(i=n.shift(),r--),o.cur=i,i&&("fx"===t&&n.unshift("inprogress"),delete o.stop,i.call(e,a,o)),!r&&o&&o.empty.fire()},_queueHooks:function(e,t){var n=t+"queueHooks";return b._data(e,n)||b._data(e,n,{empty:b.Callbacks("once memory").add(function(){b._removeData(e,t+"queue"),b._removeData(e,n)})})}}),b.fn.extend({queue:function(e,n){var r=2;return"string"!=typeof e&&(n=e,e="fx",r--),r>arguments.length?b.queue(this[0],e):n===t?this:this.each(function(){var t=b.queue(this,e,n);b._queueHooks(this,e),"fx"===e&&"inprogress"!==t[0]&&b.dequeue(this,e)})},dequeue:function(e){return this.each(function(){b.dequeue(this,e)})},delay:function(e,t){return e=b.fx?b.fx.speeds[e]||e:e,t=t||"fx",this.queue(t,function(t,n){var r=setTimeout(t,e);n.stop=function(){clearTimeout(r)}})},clearQueue:function(e){return this.queue(e||"fx",[])},promise:function(e,n){var r,i=1,o=b.Deferred(),a=this,s=this.length,u=function(){--i||o.resolveWith(a,[a])};"string"!=typeof e&&(n=e,e=t),e=e||"fx";while(s--)r=b._data(a[s],e+"queueHooks"),r&&r.empty&&(i++,r.empty.add(u));return u(),o.promise(n)}});var I,z,X=/[\t\r\n]/g,U=/\r/g,V=/^(?:input|select|textarea|button|object)$/i,Y=/^(?:a|area)$/i,J=/^(?:checked|selected|autofocus|autoplay|async|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped)$/i,G=/^(?:checked|selected)$/i,Q=b.support.getSetAttribute,K=b.support.input;b.fn.extend({attr:function(e,t){return b.access(this,b.attr,e,t,arguments.length>1)},removeAttr:function(e){return this.each(function(){b.removeAttr(this,e)})},prop:function(e,t){return b.access(this,b.prop,e,t,arguments.length>1)},removeProp:function(e){return e=b.propFix[e]||e,this.each(function(){try{this[e]=t,delete this[e]}catch(n){}})},addClass:function(e){var t,n,r,i,o,a=0,s=this.length,u="string"==typeof e&&e;if(b.isFunction(e))return this.each(function(t){b(this).addClass(e.call(this,t,this.className))});if(u)for(t=(e||"").match(w)||[];s>a;a++)if(n=this[a],r=1===n.nodeType&&(n.className?(" "+n.className+" ").replace(X," "):" ")){o=0;while(i=t[o++])0>r.indexOf(" "+i+" ")&&(r+=i+" ");n.className=b.trim(r)}return this},removeClass:function(e){var t,n,r,i,o,a=0,s=this.length,u=0===arguments.length||"string"==typeof e&&e;if(b.isFunction(e))return this.each(function(t){b(this).removeClass(e.call(this,t,this.className))});if(u)for(t=(e||"").match(w)||[];s>a;a++)if(n=this[a],r=1===n.nodeType&&(n.className?(" "+n.className+" ").replace(X," "):"")){o=0;while(i=t[o++])while(r.indexOf(" "+i+" ")>=0)r=r.replace(" "+i+" "," ");n.className=e?b.trim(r):""}return this},toggleClass:function(e,t){var n=typeof e,r="boolean"==typeof t;return b.isFunction(e)?this.each(function(n){b(this).toggleClass(e.call(this,n,this.className,t),t)}):this.each(function(){if("string"===n){var o,a=0,s=b(this),u=t,l=e.match(w)||[];while(o=l[a++])u=r?u:!s.hasClass(o),s[u?"addClass":"removeClass"](o)}else(n===i||"boolean"===n)&&(this.className&&b._data(this,"__className__",this.className),this.className=this.className||e===!1?"":b._data(this,"__className__")||"")})},hasClass:function(e){var t=" "+e+" ",n=0,r=this.length;for(;r>n;n++)if(1===this[n].nodeType&&(" "+this[n].className+" ").replace(X," ").indexOf(t)>=0)return!0;return!1},val:function(e){var n,r,i,o=this[0];{if(arguments.length)return i=b.isFunction(e),this.each(function(n){var o,a=b(this);1===this.nodeType&&(o=i?e.call(this,n,a.val()):e,null==o?o="":"number"==typeof o?o+="":b.isArray(o)&&(o=b.map(o,function(e){return null==e?"":e+""})),r=b.valHooks[this.type]||b.valHooks[this.nodeName.toLowerCase()],r&&"set"in r&&r.set(this,o,"value")!==t||(this.value=o))});if(o)return r=b.valHooks[o.type]||b.valHooks[o.nodeName.toLowerCase()],r&&"get"in r&&(n=r.get(o,"value"))!==t?n:(n=o.value,"string"==typeof n?n.replace(U,""):null==n?"":n)}}}),b.extend({valHooks:{option:{get:function(e){var t=e.attributes.value;return!t||t.specified?e.value:e.text}},select:{get:function(e){var t,n,r=e.options,i=e.selectedIndex,o="select-one"===e.type||0>i,a=o?null:[],s=o?i+1:r.length,u=0>i?s:o?i:0;for(;s>u;u++)if(n=r[u],!(!n.selected&&u!==i||(b.support.optDisabled?n.disabled:null!==n.getAttribute("disabled"))||n.parentNode.disabled&&b.nodeName(n.parentNode,"optgroup"))){if(t=b(n).val(),o)return t;a.push(t)}return a},set:function(e,t){var n=b.makeArray(t);return b(e).find("option").each(function(){this.selected=b.inArray(b(this).val(),n)>=0}),n.length||(e.selectedIndex=-1),n}}},attr:function(e,n,r){var o,a,s,u=e.nodeType;if(e&&3!==u&&8!==u&&2!==u)return typeof e.getAttribute===i?b.prop(e,n,r):(a=1!==u||!b.isXMLDoc(e),a&&(n=n.toLowerCase(),o=b.attrHooks[n]||(J.test(n)?z:I)),r===t?o&&a&&"get"in o&&null!==(s=o.get(e,n))?s:(typeof e.getAttribute!==i&&(s=e.getAttribute(n)),null==s?t:s):null!==r?o&&a&&"set"in o&&(s=o.set(e,r,n))!==t?s:(e.setAttribute(n,r+""),r):(b.removeAttr(e,n),t))},removeAttr:function(e,t){var n,r,i=0,o=t&&t.match(w);if(o&&1===e.nodeType)while(n=o[i++])r=b.propFix[n]||n,J.test(n)?!Q&&G.test(n)?e[b.camelCase("default-"+n)]=e[r]=!1:e[r]=!1:b.attr(e,n,""),e.removeAttribute(Q?n:r)},attrHooks:{type:{set:function(e,t){if(!b.support.radioValue&&"radio"===t&&b.nodeName(e,"input")){var n=e.value;return e.setAttribute("type",t),n&&(e.value=n),t}}}},propFix:{tabindex:"tabIndex",readonly:"readOnly","for":"htmlFor","class":"className",maxlength:"maxLength",cellspacing:"cellSpacing",cellpadding:"cellPadding",rowspan:"rowSpan",colspan:"colSpan",usemap:"useMap",frameborder:"frameBorder",contenteditable:"contentEditable"},prop:function(e,n,r){var i,o,a,s=e.nodeType;if(e&&3!==s&&8!==s&&2!==s)return a=1!==s||!b.isXMLDoc(e),a&&(n=b.propFix[n]||n,o=b.propHooks[n]),r!==t?o&&"set"in o&&(i=o.set(e,r,n))!==t?i:e[n]=r:o&&"get"in o&&null!==(i=o.get(e,n))?i:e[n]},propHooks:{tabIndex:{get:function(e){var n=e.getAttributeNode("tabindex");return n&&n.specified?parseInt(n.value,10):V.test(e.nodeName)||Y.test(e.nodeName)&&e.href?0:t}}}}),z={get:function(e,n){var r=b.prop(e,n),i="boolean"==typeof r&&e.getAttribute(n),o="boolean"==typeof r?K&&Q?null!=i:G.test(n)?e[b.camelCase("default-"+n)]:!!i:e.getAttributeNode(n);return o&&o.value!==!1?n.toLowerCase():t},set:function(e,t,n){return t===!1?b.removeAttr(e,n):K&&Q||!G.test(n)?e.setAttribute(!Q&&b.propFix[n]||n,n):e[b.camelCase("default-"+n)]=e[n]=!0,n}},K&&Q||(b.attrHooks.value={get:function(e,n){var r=e.getAttributeNode(n);return b.nodeName(e,"input")?e.defaultValue:r&&r.specified?r.value:t},set:function(e,n,r){return b.nodeName(e,"input")?(e.defaultValue=n,t):I&&I.set(e,n,r)}}),Q||(I=b.valHooks.button={get:function(e,n){var r=e.getAttributeNode(n);return r&&("id"===n||"name"===n||"coords"===n?""!==r.value:r.specified)?r.value:t},set:function(e,n,r){var i=e.getAttributeNode(r);return i||e.setAttributeNode(i=e.ownerDocument.createAttribute(r)),i.value=n+="","value"===r||n===e.getAttribute(r)?n:t}},b.attrHooks.contenteditable={get:I.get,set:function(e,t,n){I.set(e,""===t?!1:t,n)}},b.each(["width","height"],function(e,n){b.attrHooks[n]=b.extend(b.attrHooks[n],{set:function(e,r){return""===r?(e.setAttribute(n,"auto"),r):t}})})),b.support.hrefNormalized||(b.each(["href","src","width","height"],function(e,n){b.attrHooks[n]=b.extend(b.attrHooks[n],{get:function(e){var r=e.getAttribute(n,2);return null==r?t:r}})}),b.each(["href","src"],function(e,t){b.propHooks[t]={get:function(e){return e.getAttribute(t,4)}}})),b.support.style||(b.attrHooks.style={get:function(e){return e.style.cssText||t},set:function(e,t){return e.style.cssText=t+""}}),b.support.optSelected||(b.propHooks.selected=b.extend(b.propHooks.selected,{get:function(e){var t=e.parentNode;return t&&(t.selectedIndex,t.parentNode&&t.parentNode.selectedIndex),null}})),b.support.enctype||(b.propFix.enctype="encoding"),b.support.checkOn||b.each(["radio","checkbox"],function(){b.valHooks[this]={get:function(e){return null===e.getAttribute("value")?"on":e.value}}}),b.each(["radio","checkbox"],function(){b.valHooks[this]=b.extend(b.valHooks[this],{set:function(e,n){return b.isArray(n)?e.checked=b.inArray(b(e).val(),n)>=0:t}})});var Z=/^(?:input|select|textarea)$/i,et=/^key/,tt=/^(?:mouse|contextmenu)|click/,nt=/^(?:focusinfocus|focusoutblur)$/,rt=/^([^.]*)(?:\.(.+)|)$/;function it(){return!0}function ot(){return!1}b.event={global:{},add:function(e,n,r,o,a){var s,u,l,c,p,f,d,h,g,m,y,v=b._data(e);if(v){r.handler&&(c=r,r=c.handler,a=c.selector),r.guid||(r.guid=b.guid++),(u=v.events)||(u=v.events={}),(f=v.handle)||(f=v.handle=function(e){return typeof b===i||e&&b.event.triggered===e.type?t:b.event.dispatch.apply(f.elem,arguments)},f.elem=e),n=(n||"").match(w)||[""],l=n.length;while(l--)s=rt.exec(n[l])||[],g=y=s[1],m=(s[2]||"").split(".").sort(),p=b.event.special[g]||{},g=(a?p.delegateType:p.bindType)||g,p=b.event.special[g]||{},d=b.extend({type:g,origType:y,data:o,handler:r,guid:r.guid,selector:a,needsContext:a&&b.expr.match.needsContext.test(a),namespace:m.join(".")},c),(h=u[g])||(h=u[g]=[],h.delegateCount=0,p.setup&&p.setup.call(e,o,m,f)!==!1||(e.addEventListener?e.addEventListener(g,f,!1):e.attachEvent&&e.attachEvent("on"+g,f))),p.add&&(p.add.call(e,d),d.handler.guid||(d.handler.guid=r.guid)),a?h.splice(h.delegateCount++,0,d):h.push(d),b.event.global[g]=!0;e=null}},remove:function(e,t,n,r,i){var o,a,s,u,l,c,p,f,d,h,g,m=b.hasData(e)&&b._data(e);if(m&&(c=m.events)){t=(t||"").match(w)||[""],l=t.length;while(l--)if(s=rt.exec(t[l])||[],d=g=s[1],h=(s[2]||"").split(".").sort(),d){p=b.event.special[d]||{},d=(r?p.delegateType:p.bindType)||d,f=c[d]||[],s=s[2]&&RegExp("(^|\\.)"+h.join("\\.(?:.*\\.|)")+"(\\.|$)"),u=o=f.length;while(o--)a=f[o],!i&&g!==a.origType||n&&n.guid!==a.guid||s&&!s.test(a.namespace)||r&&r!==a.selector&&("**"!==r||!a.selector)||(f.splice(o,1),a.selector&&f.delegateCount--,p.remove&&p.remove.call(e,a));u&&!f.length&&(p.teardown&&p.teardown.call(e,h,m.handle)!==!1||b.removeEvent(e,d,m.handle),delete c[d])}else for(d in c)b.event.remove(e,d+t[l],n,r,!0);b.isEmptyObject(c)&&(delete m.handle,b._removeData(e,"events"))}},trigger:function(n,r,i,a){var s,u,l,c,p,f,d,h=[i||o],g=y.call(n,"type")?n.type:n,m=y.call(n,"namespace")?n.namespace.split("."):[];if(l=f=i=i||o,3!==i.nodeType&&8!==i.nodeType&&!nt.test(g+b.event.triggered)&&(g.indexOf(".")>=0&&(m=g.split("."),g=m.shift(),m.sort()),u=0>g.indexOf(":")&&"on"+g,n=n[b.expando]?n:new b.Event(g,"object"==typeof n&&n),n.isTrigger=!0,n.namespace=m.join("."),n.namespace_re=n.namespace?RegExp("(^|\\.)"+m.join("\\.(?:.*\\.|)")+"(\\.|$)"):null,n.result=t,n.target||(n.target=i),r=null==r?[n]:b.makeArray(r,[n]),p=b.event.special[g]||{},a||!p.trigger||p.trigger.apply(i,r)!==!1)){if(!a&&!p.noBubble&&!b.isWindow(i)){for(c=p.delegateType||g,nt.test(c+g)||(l=l.parentNode);l;l=l.parentNode)h.push(l),f=l;f===(i.ownerDocument||o)&&h.push(f.defaultView||f.parentWindow||e)}d=0;while((l=h[d++])&&!n.isPropagationStopped())n.type=d>1?c:p.bindType||g,s=(b._data(l,"events")||{})[n.type]&&b._data(l,"handle"),s&&s.apply(l,r),s=u&&l[u],s&&b.acceptData(l)&&s.apply&&s.apply(l,r)===!1&&n.preventDefault();if(n.type=g,!(a||n.isDefaultPrevented()||p._default&&p._default.apply(i.ownerDocument,r)!==!1||"click"===g&&b.nodeName(i,"a")||!b.acceptData(i)||!u||!i[g]||b.isWindow(i))){f=i[u],f&&(i[u]=null),b.event.triggered=g;try{i[g]()}catch(v){}b.event.triggered=t,f&&(i[u]=f)}return n.result}},dispatch:function(e){e=b.event.fix(e);var n,r,i,o,a,s=[],u=h.call(arguments),l=(b._data(this,"events")||{})[e.type]||[],c=b.event.special[e.type]||{};if(u[0]=e,e.delegateTarget=this,!c.preDispatch||c.preDispatch.call(this,e)!==!1){s=b.event.handlers.call(this,e,l),n=0;while((o=s[n++])&&!e.isPropagationStopped()){e.currentTarget=o.elem,a=0;while((i=o.handlers[a++])&&!e.isImmediatePropagationStopped())(!e.namespace_re||e.namespace_re.test(i.namespace))&&(e.handleObj=i,e.data=i.data,r=((b.event.special[i.origType]||{}).handle||i.handler).apply(o.elem,u),r!==t&&(e.result=r)===!1&&(e.preventDefault(),e.stopPropagation()))}return c.postDispatch&&c.postDispatch.call(this,e),e.result}},handlers:function(e,n){var r,i,o,a,s=[],u=n.delegateCount,l=e.target;if(u&&l.nodeType&&(!e.button||"click"!==e.type))for(;l!=this;l=l.parentNode||this)if(1===l.nodeType&&(l.disabled!==!0||"click"!==e.type)){for(o=[],a=0;u>a;a++)i=n[a],r=i.selector+" ",o[r]===t&&(o[r]=i.needsContext?b(r,this).index(l)>=0:b.find(r,this,null,[l]).length),o[r]&&o.push(i);o.length&&s.push({elem:l,handlers:o})}return n.length>u&&s.push({elem:this,handlers:n.slice(u)}),s},fix:function(e){if(e[b.expando])return e;var t,n,r,i=e.type,a=e,s=this.fixHooks[i];s||(this.fixHooks[i]=s=tt.test(i)?this.mouseHooks:et.test(i)?this.keyHooks:{}),r=s.props?this.props.concat(s.props):this.props,e=new b.Event(a),t=r.length;while(t--)n=r[t],e[n]=a[n];return e.target||(e.target=a.srcElement||o),3===e.target.nodeType&&(e.target=e.target.parentNode),e.metaKey=!!e.metaKey,s.filter?s.filter(e,a):e},props:"altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),fixHooks:{},keyHooks:{props:"char charCode key keyCode".split(" "),filter:function(e,t){return null==e.which&&(e.which=null!=t.charCode?t.charCode:t.keyCode),e}},mouseHooks:{props:"button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),filter:function(e,n){var r,i,a,s=n.button,u=n.fromElement;return null==e.pageX&&null!=n.clientX&&(i=e.target.ownerDocument||o,a=i.documentElement,r=i.body,e.pageX=n.clientX+(a&&a.scrollLeft||r&&r.scrollLeft||0)-(a&&a.clientLeft||r&&r.clientLeft||0),e.pageY=n.clientY+(a&&a.scrollTop||r&&r.scrollTop||0)-(a&&a.clientTop||r&&r.clientTop||0)),!e.relatedTarget&&u&&(e.relatedTarget=u===e.target?n.toElement:u),e.which||s===t||(e.which=1&s?1:2&s?3:4&s?2:0),e}},special:{load:{noBubble:!0},click:{trigger:function(){return b.nodeName(this,"input")&&"checkbox"===this.type&&this.click?(this.click(),!1):t}},focus:{trigger:function(){if(this!==o.activeElement&&this.focus)try{return this.focus(),!1}catch(e){}},delegateType:"focusin"},blur:{trigger:function(){return this===o.activeElement&&this.blur?(this.blur(),!1):t},delegateType:"focusout"},beforeunload:{postDispatch:function(e){e.result!==t&&(e.originalEvent.returnValue=e.result)}}},simulate:function(e,t,n,r){var i=b.extend(new b.Event,n,{type:e,isSimulated:!0,originalEvent:{}});r?b.event.trigger(i,null,t):b.event.dispatch.call(t,i),i.isDefaultPrevented()&&n.preventDefault()}},b.removeEvent=o.removeEventListener?function(e,t,n){e.removeEventListener&&e.removeEventListener(t,n,!1)}:function(e,t,n){var r="on"+t;e.detachEvent&&(typeof e[r]===i&&(e[r]=null),e.detachEvent(r,n))},b.Event=function(e,n){return this instanceof b.Event?(e&&e.type?(this.originalEvent=e,this.type=e.type,this.isDefaultPrevented=e.defaultPrevented||e.returnValue===!1||e.getPreventDefault&&e.getPreventDefault()?it:ot):this.type=e,n&&b.extend(this,n),this.timeStamp=e&&e.timeStamp||b.now(),this[b.expando]=!0,t):new b.Event(e,n)},b.Event.prototype={isDefaultPrevented:ot,isPropagationStopped:ot,isImmediatePropagationStopped:ot,preventDefault:function(){var e=this.originalEvent;this.isDefaultPrevented=it,e&&(e.preventDefault?e.preventDefault():e.returnValue=!1)},stopPropagation:function(){var e=this.originalEvent;this.isPropagationStopped=it,e&&(e.stopPropagation&&e.stopPropagation(),e.cancelBubble=!0)},stopImmediatePropagation:function(){this.isImmediatePropagationStopped=it,this.stopPropagation()}},b.each({mouseenter:"mouseover",mouseleave:"mouseout"},function(e,t){b.event.special[e]={delegateType:t,bindType:t,handle:function(e){var n,r=this,i=e.relatedTarget,o=e.handleObj;
return(!i||i!==r&&!b.contains(r,i))&&(e.type=o.origType,n=o.handler.apply(this,arguments),e.type=t),n}}}),b.support.submitBubbles||(b.event.special.submit={setup:function(){return b.nodeName(this,"form")?!1:(b.event.add(this,"click._submit keypress._submit",function(e){var n=e.target,r=b.nodeName(n,"input")||b.nodeName(n,"button")?n.form:t;r&&!b._data(r,"submitBubbles")&&(b.event.add(r,"submit._submit",function(e){e._submit_bubble=!0}),b._data(r,"submitBubbles",!0))}),t)},postDispatch:function(e){e._submit_bubble&&(delete e._submit_bubble,this.parentNode&&!e.isTrigger&&b.event.simulate("submit",this.parentNode,e,!0))},teardown:function(){return b.nodeName(this,"form")?!1:(b.event.remove(this,"._submit"),t)}}),b.support.changeBubbles||(b.event.special.change={setup:function(){return Z.test(this.nodeName)?(("checkbox"===this.type||"radio"===this.type)&&(b.event.add(this,"propertychange._change",function(e){"checked"===e.originalEvent.propertyName&&(this._just_changed=!0)}),b.event.add(this,"click._change",function(e){this._just_changed&&!e.isTrigger&&(this._just_changed=!1),b.event.simulate("change",this,e,!0)})),!1):(b.event.add(this,"beforeactivate._change",function(e){var t=e.target;Z.test(t.nodeName)&&!b._data(t,"changeBubbles")&&(b.event.add(t,"change._change",function(e){!this.parentNode||e.isSimulated||e.isTrigger||b.event.simulate("change",this.parentNode,e,!0)}),b._data(t,"changeBubbles",!0))}),t)},handle:function(e){var n=e.target;return this!==n||e.isSimulated||e.isTrigger||"radio"!==n.type&&"checkbox"!==n.type?e.handleObj.handler.apply(this,arguments):t},teardown:function(){return b.event.remove(this,"._change"),!Z.test(this.nodeName)}}),b.support.focusinBubbles||b.each({focus:"focusin",blur:"focusout"},function(e,t){var n=0,r=function(e){b.event.simulate(t,e.target,b.event.fix(e),!0)};b.event.special[t]={setup:function(){0===n++&&o.addEventListener(e,r,!0)},teardown:function(){0===--n&&o.removeEventListener(e,r,!0)}}}),b.fn.extend({on:function(e,n,r,i,o){var a,s;if("object"==typeof e){"string"!=typeof n&&(r=r||n,n=t);for(a in e)this.on(a,n,r,e[a],o);return this}if(null==r&&null==i?(i=n,r=n=t):null==i&&("string"==typeof n?(i=r,r=t):(i=r,r=n,n=t)),i===!1)i=ot;else if(!i)return this;return 1===o&&(s=i,i=function(e){return b().off(e),s.apply(this,arguments)},i.guid=s.guid||(s.guid=b.guid++)),this.each(function(){b.event.add(this,e,i,r,n)})},one:function(e,t,n,r){return this.on(e,t,n,r,1)},off:function(e,n,r){var i,o;if(e&&e.preventDefault&&e.handleObj)return i=e.handleObj,b(e.delegateTarget).off(i.namespace?i.origType+"."+i.namespace:i.origType,i.selector,i.handler),this;if("object"==typeof e){for(o in e)this.off(o,n,e[o]);return this}return(n===!1||"function"==typeof n)&&(r=n,n=t),r===!1&&(r=ot),this.each(function(){b.event.remove(this,e,r,n)})},bind:function(e,t,n){return this.on(e,null,t,n)},unbind:function(e,t){return this.off(e,null,t)},delegate:function(e,t,n,r){return this.on(t,e,n,r)},undelegate:function(e,t,n){return 1===arguments.length?this.off(e,"**"):this.off(t,e||"**",n)},trigger:function(e,t){return this.each(function(){b.event.trigger(e,t,this)})},triggerHandler:function(e,n){var r=this[0];return r?b.event.trigger(e,n,r,!0):t}}),function(e,t){var n,r,i,o,a,s,u,l,c,p,f,d,h,g,m,y,v,x="sizzle"+-new Date,w=e.document,T={},N=0,C=0,k=it(),E=it(),S=it(),A=typeof t,j=1<<31,D=[],L=D.pop,H=D.push,q=D.slice,M=D.indexOf||function(e){var t=0,n=this.length;for(;n>t;t++)if(this[t]===e)return t;return-1},_="[\\x20\\t\\r\\n\\f]",F="(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",O=F.replace("w","w#"),B="([*^$|!~]?=)",P="\\["+_+"*("+F+")"+_+"*(?:"+B+_+"*(?:(['\"])((?:\\\\.|[^\\\\])*?)\\3|("+O+")|)|)"+_+"*\\]",R=":("+F+")(?:\\(((['\"])((?:\\\\.|[^\\\\])*?)\\3|((?:\\\\.|[^\\\\()[\\]]|"+P.replace(3,8)+")*)|.*)\\)|)",W=RegExp("^"+_+"+|((?:^|[^\\\\])(?:\\\\.)*)"+_+"+$","g"),$=RegExp("^"+_+"*,"+_+"*"),I=RegExp("^"+_+"*([\\x20\\t\\r\\n\\f>+~])"+_+"*"),z=RegExp(R),X=RegExp("^"+O+"$"),U={ID:RegExp("^#("+F+")"),CLASS:RegExp("^\\.("+F+")"),NAME:RegExp("^\\[name=['\"]?("+F+")['\"]?\\]"),TAG:RegExp("^("+F.replace("w","w*")+")"),ATTR:RegExp("^"+P),PSEUDO:RegExp("^"+R),CHILD:RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\("+_+"*(even|odd|(([+-]|)(\\d*)n|)"+_+"*(?:([+-]|)"+_+"*(\\d+)|))"+_+"*\\)|)","i"),needsContext:RegExp("^"+_+"*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\("+_+"*((?:-\\d)?\\d*)"+_+"*\\)|)(?=[^-]|$)","i")},V=/[\x20\t\r\n\f]*[+~]/,Y=/^[^{]+\{\s*\[native code/,J=/^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,G=/^(?:input|select|textarea|button)$/i,Q=/^h\d$/i,K=/'|\\/g,Z=/\=[\x20\t\r\n\f]*([^'"\]]*)[\x20\t\r\n\f]*\]/g,et=/\\([\da-fA-F]{1,6}[\x20\t\r\n\f]?|.)/g,tt=function(e,t){var n="0x"+t-65536;return n!==n?t:0>n?String.fromCharCode(n+65536):String.fromCharCode(55296|n>>10,56320|1023&n)};try{q.call(w.documentElement.childNodes,0)[0].nodeType}catch(nt){q=function(e){var t,n=[];while(t=this[e++])n.push(t);return n}}function rt(e){return Y.test(e+"")}function it(){var e,t=[];return e=function(n,r){return t.push(n+=" ")>i.cacheLength&&delete e[t.shift()],e[n]=r}}function ot(e){return e[x]=!0,e}function at(e){var t=p.createElement("div");try{return e(t)}catch(n){return!1}finally{t=null}}function st(e,t,n,r){var i,o,a,s,u,l,f,g,m,v;if((t?t.ownerDocument||t:w)!==p&&c(t),t=t||p,n=n||[],!e||"string"!=typeof e)return n;if(1!==(s=t.nodeType)&&9!==s)return[];if(!d&&!r){if(i=J.exec(e))if(a=i[1]){if(9===s){if(o=t.getElementById(a),!o||!o.parentNode)return n;if(o.id===a)return n.push(o),n}else if(t.ownerDocument&&(o=t.ownerDocument.getElementById(a))&&y(t,o)&&o.id===a)return n.push(o),n}else{if(i[2])return H.apply(n,q.call(t.getElementsByTagName(e),0)),n;if((a=i[3])&&T.getByClassName&&t.getElementsByClassName)return H.apply(n,q.call(t.getElementsByClassName(a),0)),n}if(T.qsa&&!h.test(e)){if(f=!0,g=x,m=t,v=9===s&&e,1===s&&"object"!==t.nodeName.toLowerCase()){l=ft(e),(f=t.getAttribute("id"))?g=f.replace(K,"\\$&"):t.setAttribute("id",g),g="[id='"+g+"'] ",u=l.length;while(u--)l[u]=g+dt(l[u]);m=V.test(e)&&t.parentNode||t,v=l.join(",")}if(v)try{return H.apply(n,q.call(m.querySelectorAll(v),0)),n}catch(b){}finally{f||t.removeAttribute("id")}}}return wt(e.replace(W,"$1"),t,n,r)}a=st.isXML=function(e){var t=e&&(e.ownerDocument||e).documentElement;return t?"HTML"!==t.nodeName:!1},c=st.setDocument=function(e){var n=e?e.ownerDocument||e:w;return n!==p&&9===n.nodeType&&n.documentElement?(p=n,f=n.documentElement,d=a(n),T.tagNameNoComments=at(function(e){return e.appendChild(n.createComment("")),!e.getElementsByTagName("*").length}),T.attributes=at(function(e){e.innerHTML="<select></select>";var t=typeof e.lastChild.getAttribute("multiple");return"boolean"!==t&&"string"!==t}),T.getByClassName=at(function(e){return e.innerHTML="<div class='hidden e'></div><div class='hidden'></div>",e.getElementsByClassName&&e.getElementsByClassName("e").length?(e.lastChild.className="e",2===e.getElementsByClassName("e").length):!1}),T.getByName=at(function(e){e.id=x+0,e.innerHTML="<a name='"+x+"'></a><div name='"+x+"'></div>",f.insertBefore(e,f.firstChild);var t=n.getElementsByName&&n.getElementsByName(x).length===2+n.getElementsByName(x+0).length;return T.getIdNotName=!n.getElementById(x),f.removeChild(e),t}),i.attrHandle=at(function(e){return e.innerHTML="<a href='#'></a>",e.firstChild&&typeof e.firstChild.getAttribute!==A&&"#"===e.firstChild.getAttribute("href")})?{}:{href:function(e){return e.getAttribute("href",2)},type:function(e){return e.getAttribute("type")}},T.getIdNotName?(i.find.ID=function(e,t){if(typeof t.getElementById!==A&&!d){var n=t.getElementById(e);return n&&n.parentNode?[n]:[]}},i.filter.ID=function(e){var t=e.replace(et,tt);return function(e){return e.getAttribute("id")===t}}):(i.find.ID=function(e,n){if(typeof n.getElementById!==A&&!d){var r=n.getElementById(e);return r?r.id===e||typeof r.getAttributeNode!==A&&r.getAttributeNode("id").value===e?[r]:t:[]}},i.filter.ID=function(e){var t=e.replace(et,tt);return function(e){var n=typeof e.getAttributeNode!==A&&e.getAttributeNode("id");return n&&n.value===t}}),i.find.TAG=T.tagNameNoComments?function(e,n){return typeof n.getElementsByTagName!==A?n.getElementsByTagName(e):t}:function(e,t){var n,r=[],i=0,o=t.getElementsByTagName(e);if("*"===e){while(n=o[i++])1===n.nodeType&&r.push(n);return r}return o},i.find.NAME=T.getByName&&function(e,n){return typeof n.getElementsByName!==A?n.getElementsByName(name):t},i.find.CLASS=T.getByClassName&&function(e,n){return typeof n.getElementsByClassName===A||d?t:n.getElementsByClassName(e)},g=[],h=[":focus"],(T.qsa=rt(n.querySelectorAll))&&(at(function(e){e.innerHTML="<select><option selected=''></option></select>",e.querySelectorAll("[selected]").length||h.push("\\["+_+"*(?:checked|disabled|ismap|multiple|readonly|selected|value)"),e.querySelectorAll(":checked").length||h.push(":checked")}),at(function(e){e.innerHTML="<input type='hidden' i=''/>",e.querySelectorAll("[i^='']").length&&h.push("[*^$]="+_+"*(?:\"\"|'')"),e.querySelectorAll(":enabled").length||h.push(":enabled",":disabled"),e.querySelectorAll("*,:x"),h.push(",.*:")})),(T.matchesSelector=rt(m=f.matchesSelector||f.mozMatchesSelector||f.webkitMatchesSelector||f.oMatchesSelector||f.msMatchesSelector))&&at(function(e){T.disconnectedMatch=m.call(e,"div"),m.call(e,"[s!='']:x"),g.push("!=",R)}),h=RegExp(h.join("|")),g=RegExp(g.join("|")),y=rt(f.contains)||f.compareDocumentPosition?function(e,t){var n=9===e.nodeType?e.documentElement:e,r=t&&t.parentNode;return e===r||!(!r||1!==r.nodeType||!(n.contains?n.contains(r):e.compareDocumentPosition&&16&e.compareDocumentPosition(r)))}:function(e,t){if(t)while(t=t.parentNode)if(t===e)return!0;return!1},v=f.compareDocumentPosition?function(e,t){var r;return e===t?(u=!0,0):(r=t.compareDocumentPosition&&e.compareDocumentPosition&&e.compareDocumentPosition(t))?1&r||e.parentNode&&11===e.parentNode.nodeType?e===n||y(w,e)?-1:t===n||y(w,t)?1:0:4&r?-1:1:e.compareDocumentPosition?-1:1}:function(e,t){var r,i=0,o=e.parentNode,a=t.parentNode,s=[e],l=[t];if(e===t)return u=!0,0;if(!o||!a)return e===n?-1:t===n?1:o?-1:a?1:0;if(o===a)return ut(e,t);r=e;while(r=r.parentNode)s.unshift(r);r=t;while(r=r.parentNode)l.unshift(r);while(s[i]===l[i])i++;return i?ut(s[i],l[i]):s[i]===w?-1:l[i]===w?1:0},u=!1,[0,0].sort(v),T.detectDuplicates=u,p):p},st.matches=function(e,t){return st(e,null,null,t)},st.matchesSelector=function(e,t){if((e.ownerDocument||e)!==p&&c(e),t=t.replace(Z,"='$1']"),!(!T.matchesSelector||d||g&&g.test(t)||h.test(t)))try{var n=m.call(e,t);if(n||T.disconnectedMatch||e.document&&11!==e.document.nodeType)return n}catch(r){}return st(t,p,null,[e]).length>0},st.contains=function(e,t){return(e.ownerDocument||e)!==p&&c(e),y(e,t)},st.attr=function(e,t){var n;return(e.ownerDocument||e)!==p&&c(e),d||(t=t.toLowerCase()),(n=i.attrHandle[t])?n(e):d||T.attributes?e.getAttribute(t):((n=e.getAttributeNode(t))||e.getAttribute(t))&&e[t]===!0?t:n&&n.specified?n.value:null},st.error=function(e){throw Error("Syntax error, unrecognized expression: "+e)},st.uniqueSort=function(e){var t,n=[],r=1,i=0;if(u=!T.detectDuplicates,e.sort(v),u){for(;t=e[r];r++)t===e[r-1]&&(i=n.push(r));while(i--)e.splice(n[i],1)}return e};function ut(e,t){var n=t&&e,r=n&&(~t.sourceIndex||j)-(~e.sourceIndex||j);if(r)return r;if(n)while(n=n.nextSibling)if(n===t)return-1;return e?1:-1}function lt(e){return function(t){var n=t.nodeName.toLowerCase();return"input"===n&&t.type===e}}function ct(e){return function(t){var n=t.nodeName.toLowerCase();return("input"===n||"button"===n)&&t.type===e}}function pt(e){return ot(function(t){return t=+t,ot(function(n,r){var i,o=e([],n.length,t),a=o.length;while(a--)n[i=o[a]]&&(n[i]=!(r[i]=n[i]))})})}o=st.getText=function(e){var t,n="",r=0,i=e.nodeType;if(i){if(1===i||9===i||11===i){if("string"==typeof e.textContent)return e.textContent;for(e=e.firstChild;e;e=e.nextSibling)n+=o(e)}else if(3===i||4===i)return e.nodeValue}else for(;t=e[r];r++)n+=o(t);return n},i=st.selectors={cacheLength:50,createPseudo:ot,match:U,find:{},relative:{">":{dir:"parentNode",first:!0}," ":{dir:"parentNode"},"+":{dir:"previousSibling",first:!0},"~":{dir:"previousSibling"}},preFilter:{ATTR:function(e){return e[1]=e[1].replace(et,tt),e[3]=(e[4]||e[5]||"").replace(et,tt),"~="===e[2]&&(e[3]=" "+e[3]+" "),e.slice(0,4)},CHILD:function(e){return e[1]=e[1].toLowerCase(),"nth"===e[1].slice(0,3)?(e[3]||st.error(e[0]),e[4]=+(e[4]?e[5]+(e[6]||1):2*("even"===e[3]||"odd"===e[3])),e[5]=+(e[7]+e[8]||"odd"===e[3])):e[3]&&st.error(e[0]),e},PSEUDO:function(e){var t,n=!e[5]&&e[2];return U.CHILD.test(e[0])?null:(e[4]?e[2]=e[4]:n&&z.test(n)&&(t=ft(n,!0))&&(t=n.indexOf(")",n.length-t)-n.length)&&(e[0]=e[0].slice(0,t),e[2]=n.slice(0,t)),e.slice(0,3))}},filter:{TAG:function(e){return"*"===e?function(){return!0}:(e=e.replace(et,tt).toLowerCase(),function(t){return t.nodeName&&t.nodeName.toLowerCase()===e})},CLASS:function(e){var t=k[e+" "];return t||(t=RegExp("(^|"+_+")"+e+"("+_+"|$)"))&&k(e,function(e){return t.test(e.className||typeof e.getAttribute!==A&&e.getAttribute("class")||"")})},ATTR:function(e,t,n){return function(r){var i=st.attr(r,e);return null==i?"!="===t:t?(i+="","="===t?i===n:"!="===t?i!==n:"^="===t?n&&0===i.indexOf(n):"*="===t?n&&i.indexOf(n)>-1:"$="===t?n&&i.slice(-n.length)===n:"~="===t?(" "+i+" ").indexOf(n)>-1:"|="===t?i===n||i.slice(0,n.length+1)===n+"-":!1):!0}},CHILD:function(e,t,n,r,i){var o="nth"!==e.slice(0,3),a="last"!==e.slice(-4),s="of-type"===t;return 1===r&&0===i?function(e){return!!e.parentNode}:function(t,n,u){var l,c,p,f,d,h,g=o!==a?"nextSibling":"previousSibling",m=t.parentNode,y=s&&t.nodeName.toLowerCase(),v=!u&&!s;if(m){if(o){while(g){p=t;while(p=p[g])if(s?p.nodeName.toLowerCase()===y:1===p.nodeType)return!1;h=g="only"===e&&!h&&"nextSibling"}return!0}if(h=[a?m.firstChild:m.lastChild],a&&v){c=m[x]||(m[x]={}),l=c[e]||[],d=l[0]===N&&l[1],f=l[0]===N&&l[2],p=d&&m.childNodes[d];while(p=++d&&p&&p[g]||(f=d=0)||h.pop())if(1===p.nodeType&&++f&&p===t){c[e]=[N,d,f];break}}else if(v&&(l=(t[x]||(t[x]={}))[e])&&l[0]===N)f=l[1];else while(p=++d&&p&&p[g]||(f=d=0)||h.pop())if((s?p.nodeName.toLowerCase()===y:1===p.nodeType)&&++f&&(v&&((p[x]||(p[x]={}))[e]=[N,f]),p===t))break;return f-=i,f===r||0===f%r&&f/r>=0}}},PSEUDO:function(e,t){var n,r=i.pseudos[e]||i.setFilters[e.toLowerCase()]||st.error("unsupported pseudo: "+e);return r[x]?r(t):r.length>1?(n=[e,e,"",t],i.setFilters.hasOwnProperty(e.toLowerCase())?ot(function(e,n){var i,o=r(e,t),a=o.length;while(a--)i=M.call(e,o[a]),e[i]=!(n[i]=o[a])}):function(e){return r(e,0,n)}):r}},pseudos:{not:ot(function(e){var t=[],n=[],r=s(e.replace(W,"$1"));return r[x]?ot(function(e,t,n,i){var o,a=r(e,null,i,[]),s=e.length;while(s--)(o=a[s])&&(e[s]=!(t[s]=o))}):function(e,i,o){return t[0]=e,r(t,null,o,n),!n.pop()}}),has:ot(function(e){return function(t){return st(e,t).length>0}}),contains:ot(function(e){return function(t){return(t.textContent||t.innerText||o(t)).indexOf(e)>-1}}),lang:ot(function(e){return X.test(e||"")||st.error("unsupported lang: "+e),e=e.replace(et,tt).toLowerCase(),function(t){var n;do if(n=d?t.getAttribute("xml:lang")||t.getAttribute("lang"):t.lang)return n=n.toLowerCase(),n===e||0===n.indexOf(e+"-");while((t=t.parentNode)&&1===t.nodeType);return!1}}),target:function(t){var n=e.location&&e.location.hash;return n&&n.slice(1)===t.id},root:function(e){return e===f},focus:function(e){return e===p.activeElement&&(!p.hasFocus||p.hasFocus())&&!!(e.type||e.href||~e.tabIndex)},enabled:function(e){return e.disabled===!1},disabled:function(e){return e.disabled===!0},checked:function(e){var t=e.nodeName.toLowerCase();return"input"===t&&!!e.checked||"option"===t&&!!e.selected},selected:function(e){return e.parentNode&&e.parentNode.selectedIndex,e.selected===!0},empty:function(e){for(e=e.firstChild;e;e=e.nextSibling)if(e.nodeName>"@"||3===e.nodeType||4===e.nodeType)return!1;return!0},parent:function(e){return!i.pseudos.empty(e)},header:function(e){return Q.test(e.nodeName)},input:function(e){return G.test(e.nodeName)},button:function(e){var t=e.nodeName.toLowerCase();return"input"===t&&"button"===e.type||"button"===t},text:function(e){var t;return"input"===e.nodeName.toLowerCase()&&"text"===e.type&&(null==(t=e.getAttribute("type"))||t.toLowerCase()===e.type)},first:pt(function(){return[0]}),last:pt(function(e,t){return[t-1]}),eq:pt(function(e,t,n){return[0>n?n+t:n]}),even:pt(function(e,t){var n=0;for(;t>n;n+=2)e.push(n);return e}),odd:pt(function(e,t){var n=1;for(;t>n;n+=2)e.push(n);return e}),lt:pt(function(e,t,n){var r=0>n?n+t:n;for(;--r>=0;)e.push(r);return e}),gt:pt(function(e,t,n){var r=0>n?n+t:n;for(;t>++r;)e.push(r);return e})}};for(n in{radio:!0,checkbox:!0,file:!0,password:!0,image:!0})i.pseudos[n]=lt(n);for(n in{submit:!0,reset:!0})i.pseudos[n]=ct(n);function ft(e,t){var n,r,o,a,s,u,l,c=E[e+" "];if(c)return t?0:c.slice(0);s=e,u=[],l=i.preFilter;while(s){(!n||(r=$.exec(s)))&&(r&&(s=s.slice(r[0].length)||s),u.push(o=[])),n=!1,(r=I.exec(s))&&(n=r.shift(),o.push({value:n,type:r[0].replace(W," ")}),s=s.slice(n.length));for(a in i.filter)!(r=U[a].exec(s))||l[a]&&!(r=l[a](r))||(n=r.shift(),o.push({value:n,type:a,matches:r}),s=s.slice(n.length));if(!n)break}return t?s.length:s?st.error(e):E(e,u).slice(0)}function dt(e){var t=0,n=e.length,r="";for(;n>t;t++)r+=e[t].value;return r}function ht(e,t,n){var i=t.dir,o=n&&"parentNode"===i,a=C++;return t.first?function(t,n,r){while(t=t[i])if(1===t.nodeType||o)return e(t,n,r)}:function(t,n,s){var u,l,c,p=N+" "+a;if(s){while(t=t[i])if((1===t.nodeType||o)&&e(t,n,s))return!0}else while(t=t[i])if(1===t.nodeType||o)if(c=t[x]||(t[x]={}),(l=c[i])&&l[0]===p){if((u=l[1])===!0||u===r)return u===!0}else if(l=c[i]=[p],l[1]=e(t,n,s)||r,l[1]===!0)return!0}}function gt(e){return e.length>1?function(t,n,r){var i=e.length;while(i--)if(!e[i](t,n,r))return!1;return!0}:e[0]}function mt(e,t,n,r,i){var o,a=[],s=0,u=e.length,l=null!=t;for(;u>s;s++)(o=e[s])&&(!n||n(o,r,i))&&(a.push(o),l&&t.push(s));return a}function yt(e,t,n,r,i,o){return r&&!r[x]&&(r=yt(r)),i&&!i[x]&&(i=yt(i,o)),ot(function(o,a,s,u){var l,c,p,f=[],d=[],h=a.length,g=o||xt(t||"*",s.nodeType?[s]:s,[]),m=!e||!o&&t?g:mt(g,f,e,s,u),y=n?i||(o?e:h||r)?[]:a:m;if(n&&n(m,y,s,u),r){l=mt(y,d),r(l,[],s,u),c=l.length;while(c--)(p=l[c])&&(y[d[c]]=!(m[d[c]]=p))}if(o){if(i||e){if(i){l=[],c=y.length;while(c--)(p=y[c])&&l.push(m[c]=p);i(null,y=[],l,u)}c=y.length;while(c--)(p=y[c])&&(l=i?M.call(o,p):f[c])>-1&&(o[l]=!(a[l]=p))}}else y=mt(y===a?y.splice(h,y.length):y),i?i(null,a,y,u):H.apply(a,y)})}function vt(e){var t,n,r,o=e.length,a=i.relative[e[0].type],s=a||i.relative[" "],u=a?1:0,c=ht(function(e){return e===t},s,!0),p=ht(function(e){return M.call(t,e)>-1},s,!0),f=[function(e,n,r){return!a&&(r||n!==l)||((t=n).nodeType?c(e,n,r):p(e,n,r))}];for(;o>u;u++)if(n=i.relative[e[u].type])f=[ht(gt(f),n)];else{if(n=i.filter[e[u].type].apply(null,e[u].matches),n[x]){for(r=++u;o>r;r++)if(i.relative[e[r].type])break;return yt(u>1&&gt(f),u>1&&dt(e.slice(0,u-1)).replace(W,"$1"),n,r>u&&vt(e.slice(u,r)),o>r&&vt(e=e.slice(r)),o>r&&dt(e))}f.push(n)}return gt(f)}function bt(e,t){var n=0,o=t.length>0,a=e.length>0,s=function(s,u,c,f,d){var h,g,m,y=[],v=0,b="0",x=s&&[],w=null!=d,T=l,C=s||a&&i.find.TAG("*",d&&u.parentNode||u),k=N+=null==T?1:Math.random()||.1;for(w&&(l=u!==p&&u,r=n);null!=(h=C[b]);b++){if(a&&h){g=0;while(m=e[g++])if(m(h,u,c)){f.push(h);break}w&&(N=k,r=++n)}o&&((h=!m&&h)&&v--,s&&x.push(h))}if(v+=b,o&&b!==v){g=0;while(m=t[g++])m(x,y,u,c);if(s){if(v>0)while(b--)x[b]||y[b]||(y[b]=L.call(f));y=mt(y)}H.apply(f,y),w&&!s&&y.length>0&&v+t.length>1&&st.uniqueSort(f)}return w&&(N=k,l=T),x};return o?ot(s):s}s=st.compile=function(e,t){var n,r=[],i=[],o=S[e+" "];if(!o){t||(t=ft(e)),n=t.length;while(n--)o=vt(t[n]),o[x]?r.push(o):i.push(o);o=S(e,bt(i,r))}return o};function xt(e,t,n){var r=0,i=t.length;for(;i>r;r++)st(e,t[r],n);return n}function wt(e,t,n,r){var o,a,u,l,c,p=ft(e);if(!r&&1===p.length){if(a=p[0]=p[0].slice(0),a.length>2&&"ID"===(u=a[0]).type&&9===t.nodeType&&!d&&i.relative[a[1].type]){if(t=i.find.ID(u.matches[0].replace(et,tt),t)[0],!t)return n;e=e.slice(a.shift().value.length)}o=U.needsContext.test(e)?0:a.length;while(o--){if(u=a[o],i.relative[l=u.type])break;if((c=i.find[l])&&(r=c(u.matches[0].replace(et,tt),V.test(a[0].type)&&t.parentNode||t))){if(a.splice(o,1),e=r.length&&dt(a),!e)return H.apply(n,q.call(r,0)),n;break}}}return s(e,p)(r,t,d,n,V.test(e)),n}i.pseudos.nth=i.pseudos.eq;function Tt(){}i.filters=Tt.prototype=i.pseudos,i.setFilters=new Tt,c(),st.attr=b.attr,b.find=st,b.expr=st.selectors,b.expr[":"]=b.expr.pseudos,b.unique=st.uniqueSort,b.text=st.getText,b.isXMLDoc=st.isXML,b.contains=st.contains}(e);var at=/Until$/,st=/^(?:parents|prev(?:Until|All))/,ut=/^.[^:#\[\.,]*$/,lt=b.expr.match.needsContext,ct={children:!0,contents:!0,next:!0,prev:!0};b.fn.extend({find:function(e){var t,n,r,i=this.length;if("string"!=typeof e)return r=this,this.pushStack(b(e).filter(function(){for(t=0;i>t;t++)if(b.contains(r[t],this))return!0}));for(n=[],t=0;i>t;t++)b.find(e,this[t],n);return n=this.pushStack(i>1?b.unique(n):n),n.selector=(this.selector?this.selector+" ":"")+e,n},has:function(e){var t,n=b(e,this),r=n.length;return this.filter(function(){for(t=0;r>t;t++)if(b.contains(this,n[t]))return!0})},not:function(e){return this.pushStack(ft(this,e,!1))},filter:function(e){return this.pushStack(ft(this,e,!0))},is:function(e){return!!e&&("string"==typeof e?lt.test(e)?b(e,this.context).index(this[0])>=0:b.filter(e,this).length>0:this.filter(e).length>0)},closest:function(e,t){var n,r=0,i=this.length,o=[],a=lt.test(e)||"string"!=typeof e?b(e,t||this.context):0;for(;i>r;r++){n=this[r];while(n&&n.ownerDocument&&n!==t&&11!==n.nodeType){if(a?a.index(n)>-1:b.find.matchesSelector(n,e)){o.push(n);break}n=n.parentNode}}return this.pushStack(o.length>1?b.unique(o):o)},index:function(e){return e?"string"==typeof e?b.inArray(this[0],b(e)):b.inArray(e.jquery?e[0]:e,this):this[0]&&this[0].parentNode?this.first().prevAll().length:-1},add:function(e,t){var n="string"==typeof e?b(e,t):b.makeArray(e&&e.nodeType?[e]:e),r=b.merge(this.get(),n);return this.pushStack(b.unique(r))},addBack:function(e){return this.add(null==e?this.prevObject:this.prevObject.filter(e))}}),b.fn.andSelf=b.fn.addBack;function pt(e,t){do e=e[t];while(e&&1!==e.nodeType);return e}b.each({parent:function(e){var t=e.parentNode;return t&&11!==t.nodeType?t:null},parents:function(e){return b.dir(e,"parentNode")},parentsUntil:function(e,t,n){return b.dir(e,"parentNode",n)},next:function(e){return pt(e,"nextSibling")},prev:function(e){return pt(e,"previousSibling")},nextAll:function(e){return b.dir(e,"nextSibling")},prevAll:function(e){return b.dir(e,"previousSibling")},nextUntil:function(e,t,n){return b.dir(e,"nextSibling",n)},prevUntil:function(e,t,n){return b.dir(e,"previousSibling",n)},siblings:function(e){return b.sibling((e.parentNode||{}).firstChild,e)},children:function(e){return b.sibling(e.firstChild)},contents:function(e){return b.nodeName(e,"iframe")?e.contentDocument||e.contentWindow.document:b.merge([],e.childNodes)}},function(e,t){b.fn[e]=function(n,r){var i=b.map(this,t,n);return at.test(e)||(r=n),r&&"string"==typeof r&&(i=b.filter(r,i)),i=this.length>1&&!ct[e]?b.unique(i):i,this.length>1&&st.test(e)&&(i=i.reverse()),this.pushStack(i)}}),b.extend({filter:function(e,t,n){return n&&(e=":not("+e+")"),1===t.length?b.find.matchesSelector(t[0],e)?[t[0]]:[]:b.find.matches(e,t)},dir:function(e,n,r){var i=[],o=e[n];while(o&&9!==o.nodeType&&(r===t||1!==o.nodeType||!b(o).is(r)))1===o.nodeType&&i.push(o),o=o[n];return i},sibling:function(e,t){var n=[];for(;e;e=e.nextSibling)1===e.nodeType&&e!==t&&n.push(e);return n}});function ft(e,t,n){if(t=t||0,b.isFunction(t))return b.grep(e,function(e,r){var i=!!t.call(e,r,e);return i===n});if(t.nodeType)return b.grep(e,function(e){return e===t===n});if("string"==typeof t){var r=b.grep(e,function(e){return 1===e.nodeType});if(ut.test(t))return b.filter(t,r,!n);t=b.filter(t,r)}return b.grep(e,function(e){return b.inArray(e,t)>=0===n})}function dt(e){var t=ht.split("|"),n=e.createDocumentFragment();if(n.createElement)while(t.length)n.createElement(t.pop());return n}var ht="abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",gt=/ jQuery\d+="(?:null|\d+)"/g,mt=RegExp("<(?:"+ht+")[\\s/>]","i"),yt=/^\s+/,vt=/<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi,bt=/<([\w:]+)/,xt=/<tbody/i,wt=/<|&#?\w+;/,Tt=/<(?:script|style|link)/i,Nt=/^(?:checkbox|radio)$/i,Ct=/checked\s*(?:[^=]|=\s*.checked.)/i,kt=/^$|\/(?:java|ecma)script/i,Et=/^true\/(.*)/,St=/^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,At={option:[1,"<select multiple='multiple'>","</select>"],legend:[1,"<fieldset>","</fieldset>"],area:[1,"<map>","</map>"],param:[1,"<object>","</object>"],thead:[1,"<table>","</table>"],tr:[2,"<table><tbody>","</tbody></table>"],col:[2,"<table><tbody></tbody><colgroup>","</colgroup></table>"],td:[3,"<table><tbody><tr>","</tr></tbody></table>"],_default:b.support.htmlSerialize?[0,"",""]:[1,"X<div>","</div>"]},jt=dt(o),Dt=jt.appendChild(o.createElement("div"));At.optgroup=At.option,At.tbody=At.tfoot=At.colgroup=At.caption=At.thead,At.th=At.td,b.fn.extend({text:function(e){return b.access(this,function(e){return e===t?b.text(this):this.empty().append((this[0]&&this[0].ownerDocument||o).createTextNode(e))},null,e,arguments.length)},wrapAll:function(e){if(b.isFunction(e))return this.each(function(t){b(this).wrapAll(e.call(this,t))});if(this[0]){var t=b(e,this[0].ownerDocument).eq(0).clone(!0);this[0].parentNode&&t.insertBefore(this[0]),t.map(function(){var e=this;while(e.firstChild&&1===e.firstChild.nodeType)e=e.firstChild;return e}).append(this)}return this},wrapInner:function(e){return b.isFunction(e)?this.each(function(t){b(this).wrapInner(e.call(this,t))}):this.each(function(){var t=b(this),n=t.contents();n.length?n.wrapAll(e):t.append(e)})},wrap:function(e){var t=b.isFunction(e);return this.each(function(n){b(this).wrapAll(t?e.call(this,n):e)})},unwrap:function(){return this.parent().each(function(){b.nodeName(this,"body")||b(this).replaceWith(this.childNodes)}).end()},append:function(){return this.domManip(arguments,!0,function(e){(1===this.nodeType||11===this.nodeType||9===this.nodeType)&&this.appendChild(e)})},prepend:function(){return this.domManip(arguments,!0,function(e){(1===this.nodeType||11===this.nodeType||9===this.nodeType)&&this.insertBefore(e,this.firstChild)})},before:function(){return this.domManip(arguments,!1,function(e){this.parentNode&&this.parentNode.insertBefore(e,this)})},after:function(){return this.domManip(arguments,!1,function(e){this.parentNode&&this.parentNode.insertBefore(e,this.nextSibling)})},remove:function(e,t){var n,r=0;for(;null!=(n=this[r]);r++)(!e||b.filter(e,[n]).length>0)&&(t||1!==n.nodeType||b.cleanData(Ot(n)),n.parentNode&&(t&&b.contains(n.ownerDocument,n)&&Mt(Ot(n,"script")),n.parentNode.removeChild(n)));return this},empty:function(){var e,t=0;for(;null!=(e=this[t]);t++){1===e.nodeType&&b.cleanData(Ot(e,!1));while(e.firstChild)e.removeChild(e.firstChild);e.options&&b.nodeName(e,"select")&&(e.options.length=0)}return this},clone:function(e,t){return e=null==e?!1:e,t=null==t?e:t,this.map(function(){return b.clone(this,e,t)})},html:function(e){return b.access(this,function(e){var n=this[0]||{},r=0,i=this.length;if(e===t)return 1===n.nodeType?n.innerHTML.replace(gt,""):t;if(!("string"!=typeof e||Tt.test(e)||!b.support.htmlSerialize&&mt.test(e)||!b.support.leadingWhitespace&&yt.test(e)||At[(bt.exec(e)||["",""])[1].toLowerCase()])){e=e.replace(vt,"<$1></$2>");try{for(;i>r;r++)n=this[r]||{},1===n.nodeType&&(b.cleanData(Ot(n,!1)),n.innerHTML=e);n=0}catch(o){}}n&&this.empty().append(e)},null,e,arguments.length)},replaceWith:function(e){var t=b.isFunction(e);return t||"string"==typeof e||(e=b(e).not(this).detach()),this.domManip([e],!0,function(e){var t=this.nextSibling,n=this.parentNode;n&&(b(this).remove(),n.insertBefore(e,t))})},detach:function(e){return this.remove(e,!0)},domManip:function(e,n,r){e=f.apply([],e);var i,o,a,s,u,l,c=0,p=this.length,d=this,h=p-1,g=e[0],m=b.isFunction(g);if(m||!(1>=p||"string"!=typeof g||b.support.checkClone)&&Ct.test(g))return this.each(function(i){var o=d.eq(i);m&&(e[0]=g.call(this,i,n?o.html():t)),o.domManip(e,n,r)});if(p&&(l=b.buildFragment(e,this[0].ownerDocument,!1,this),i=l.firstChild,1===l.childNodes.length&&(l=i),i)){for(n=n&&b.nodeName(i,"tr"),s=b.map(Ot(l,"script"),Ht),a=s.length;p>c;c++)o=l,c!==h&&(o=b.clone(o,!0,!0),a&&b.merge(s,Ot(o,"script"))),r.call(n&&b.nodeName(this[c],"table")?Lt(this[c],"tbody"):this[c],o,c);if(a)for(u=s[s.length-1].ownerDocument,b.map(s,qt),c=0;a>c;c++)o=s[c],kt.test(o.type||"")&&!b._data(o,"globalEval")&&b.contains(u,o)&&(o.src?b.ajax({url:o.src,type:"GET",dataType:"script",async:!1,global:!1,"throws":!0}):b.globalEval((o.text||o.textContent||o.innerHTML||"").replace(St,"")));l=i=null}return this}});function Lt(e,t){return e.getElementsByTagName(t)[0]||e.appendChild(e.ownerDocument.createElement(t))}function Ht(e){var t=e.getAttributeNode("type");return e.type=(t&&t.specified)+"/"+e.type,e}function qt(e){var t=Et.exec(e.type);return t?e.type=t[1]:e.removeAttribute("type"),e}function Mt(e,t){var n,r=0;for(;null!=(n=e[r]);r++)b._data(n,"globalEval",!t||b._data(t[r],"globalEval"))}function _t(e,t){if(1===t.nodeType&&b.hasData(e)){var n,r,i,o=b._data(e),a=b._data(t,o),s=o.events;if(s){delete a.handle,a.events={};for(n in s)for(r=0,i=s[n].length;i>r;r++)b.event.add(t,n,s[n][r])}a.data&&(a.data=b.extend({},a.data))}}function Ft(e,t){var n,r,i;if(1===t.nodeType){if(n=t.nodeName.toLowerCase(),!b.support.noCloneEvent&&t[b.expando]){i=b._data(t);for(r in i.events)b.removeEvent(t,r,i.handle);t.removeAttribute(b.expando)}"script"===n&&t.text!==e.text?(Ht(t).text=e.text,qt(t)):"object"===n?(t.parentNode&&(t.outerHTML=e.outerHTML),b.support.html5Clone&&e.innerHTML&&!b.trim(t.innerHTML)&&(t.innerHTML=e.innerHTML)):"input"===n&&Nt.test(e.type)?(t.defaultChecked=t.checked=e.checked,t.value!==e.value&&(t.value=e.value)):"option"===n?t.defaultSelected=t.selected=e.defaultSelected:("input"===n||"textarea"===n)&&(t.defaultValue=e.defaultValue)}}b.each({appendTo:"append",prependTo:"prepend",insertBefore:"before",insertAfter:"after",replaceAll:"replaceWith"},function(e,t){b.fn[e]=function(e){var n,r=0,i=[],o=b(e),a=o.length-1;for(;a>=r;r++)n=r===a?this:this.clone(!0),b(o[r])[t](n),d.apply(i,n.get());return this.pushStack(i)}});function Ot(e,n){var r,o,a=0,s=typeof e.getElementsByTagName!==i?e.getElementsByTagName(n||"*"):typeof e.querySelectorAll!==i?e.querySelectorAll(n||"*"):t;if(!s)for(s=[],r=e.childNodes||e;null!=(o=r[a]);a++)!n||b.nodeName(o,n)?s.push(o):b.merge(s,Ot(o,n));return n===t||n&&b.nodeName(e,n)?b.merge([e],s):s}function Bt(e){Nt.test(e.type)&&(e.defaultChecked=e.checked)}b.extend({clone:function(e,t,n){var r,i,o,a,s,u=b.contains(e.ownerDocument,e);if(b.support.html5Clone||b.isXMLDoc(e)||!mt.test("<"+e.nodeName+">")?o=e.cloneNode(!0):(Dt.innerHTML=e.outerHTML,Dt.removeChild(o=Dt.firstChild)),!(b.support.noCloneEvent&&b.support.noCloneChecked||1!==e.nodeType&&11!==e.nodeType||b.isXMLDoc(e)))for(r=Ot(o),s=Ot(e),a=0;null!=(i=s[a]);++a)r[a]&&Ft(i,r[a]);if(t)if(n)for(s=s||Ot(e),r=r||Ot(o),a=0;null!=(i=s[a]);a++)_t(i,r[a]);else _t(e,o);return r=Ot(o,"script"),r.length>0&&Mt(r,!u&&Ot(e,"script")),r=s=i=null,o},buildFragment:function(e,t,n,r){var i,o,a,s,u,l,c,p=e.length,f=dt(t),d=[],h=0;for(;p>h;h++)if(o=e[h],o||0===o)if("object"===b.type(o))b.merge(d,o.nodeType?[o]:o);else if(wt.test(o)){s=s||f.appendChild(t.createElement("div")),u=(bt.exec(o)||["",""])[1].toLowerCase(),c=At[u]||At._default,s.innerHTML=c[1]+o.replace(vt,"<$1></$2>")+c[2],i=c[0];while(i--)s=s.lastChild;if(!b.support.leadingWhitespace&&yt.test(o)&&d.push(t.createTextNode(yt.exec(o)[0])),!b.support.tbody){o="table"!==u||xt.test(o)?"<table>"!==c[1]||xt.test(o)?0:s:s.firstChild,i=o&&o.childNodes.length;while(i--)b.nodeName(l=o.childNodes[i],"tbody")&&!l.childNodes.length&&o.removeChild(l)
}b.merge(d,s.childNodes),s.textContent="";while(s.firstChild)s.removeChild(s.firstChild);s=f.lastChild}else d.push(t.createTextNode(o));s&&f.removeChild(s),b.support.appendChecked||b.grep(Ot(d,"input"),Bt),h=0;while(o=d[h++])if((!r||-1===b.inArray(o,r))&&(a=b.contains(o.ownerDocument,o),s=Ot(f.appendChild(o),"script"),a&&Mt(s),n)){i=0;while(o=s[i++])kt.test(o.type||"")&&n.push(o)}return s=null,f},cleanData:function(e,t){var n,r,o,a,s=0,u=b.expando,l=b.cache,p=b.support.deleteExpando,f=b.event.special;for(;null!=(n=e[s]);s++)if((t||b.acceptData(n))&&(o=n[u],a=o&&l[o])){if(a.events)for(r in a.events)f[r]?b.event.remove(n,r):b.removeEvent(n,r,a.handle);l[o]&&(delete l[o],p?delete n[u]:typeof n.removeAttribute!==i?n.removeAttribute(u):n[u]=null,c.push(o))}}});var Pt,Rt,Wt,$t=/alpha\([^)]*\)/i,It=/opacity\s*=\s*([^)]*)/,zt=/^(top|right|bottom|left)$/,Xt=/^(none|table(?!-c[ea]).+)/,Ut=/^margin/,Vt=RegExp("^("+x+")(.*)$","i"),Yt=RegExp("^("+x+")(?!px)[a-z%]+$","i"),Jt=RegExp("^([+-])=("+x+")","i"),Gt={BODY:"block"},Qt={position:"absolute",visibility:"hidden",display:"block"},Kt={letterSpacing:0,fontWeight:400},Zt=["Top","Right","Bottom","Left"],en=["Webkit","O","Moz","ms"];function tn(e,t){if(t in e)return t;var n=t.charAt(0).toUpperCase()+t.slice(1),r=t,i=en.length;while(i--)if(t=en[i]+n,t in e)return t;return r}function nn(e,t){return e=t||e,"none"===b.css(e,"display")||!b.contains(e.ownerDocument,e)}function rn(e,t){var n,r,i,o=[],a=0,s=e.length;for(;s>a;a++)r=e[a],r.style&&(o[a]=b._data(r,"olddisplay"),n=r.style.display,t?(o[a]||"none"!==n||(r.style.display=""),""===r.style.display&&nn(r)&&(o[a]=b._data(r,"olddisplay",un(r.nodeName)))):o[a]||(i=nn(r),(n&&"none"!==n||!i)&&b._data(r,"olddisplay",i?n:b.css(r,"display"))));for(a=0;s>a;a++)r=e[a],r.style&&(t&&"none"!==r.style.display&&""!==r.style.display||(r.style.display=t?o[a]||"":"none"));return e}b.fn.extend({css:function(e,n){return b.access(this,function(e,n,r){var i,o,a={},s=0;if(b.isArray(n)){for(o=Rt(e),i=n.length;i>s;s++)a[n[s]]=b.css(e,n[s],!1,o);return a}return r!==t?b.style(e,n,r):b.css(e,n)},e,n,arguments.length>1)},show:function(){return rn(this,!0)},hide:function(){return rn(this)},toggle:function(e){var t="boolean"==typeof e;return this.each(function(){(t?e:nn(this))?b(this).show():b(this).hide()})}}),b.extend({cssHooks:{opacity:{get:function(e,t){if(t){var n=Wt(e,"opacity");return""===n?"1":n}}}},cssNumber:{columnCount:!0,fillOpacity:!0,fontWeight:!0,lineHeight:!0,opacity:!0,orphans:!0,widows:!0,zIndex:!0,zoom:!0},cssProps:{"float":b.support.cssFloat?"cssFloat":"styleFloat"},style:function(e,n,r,i){if(e&&3!==e.nodeType&&8!==e.nodeType&&e.style){var o,a,s,u=b.camelCase(n),l=e.style;if(n=b.cssProps[u]||(b.cssProps[u]=tn(l,u)),s=b.cssHooks[n]||b.cssHooks[u],r===t)return s&&"get"in s&&(o=s.get(e,!1,i))!==t?o:l[n];if(a=typeof r,"string"===a&&(o=Jt.exec(r))&&(r=(o[1]+1)*o[2]+parseFloat(b.css(e,n)),a="number"),!(null==r||"number"===a&&isNaN(r)||("number"!==a||b.cssNumber[u]||(r+="px"),b.support.clearCloneStyle||""!==r||0!==n.indexOf("background")||(l[n]="inherit"),s&&"set"in s&&(r=s.set(e,r,i))===t)))try{l[n]=r}catch(c){}}},css:function(e,n,r,i){var o,a,s,u=b.camelCase(n);return n=b.cssProps[u]||(b.cssProps[u]=tn(e.style,u)),s=b.cssHooks[n]||b.cssHooks[u],s&&"get"in s&&(a=s.get(e,!0,r)),a===t&&(a=Wt(e,n,i)),"normal"===a&&n in Kt&&(a=Kt[n]),""===r||r?(o=parseFloat(a),r===!0||b.isNumeric(o)?o||0:a):a},swap:function(e,t,n,r){var i,o,a={};for(o in t)a[o]=e.style[o],e.style[o]=t[o];i=n.apply(e,r||[]);for(o in t)e.style[o]=a[o];return i}}),e.getComputedStyle?(Rt=function(t){return e.getComputedStyle(t,null)},Wt=function(e,n,r){var i,o,a,s=r||Rt(e),u=s?s.getPropertyValue(n)||s[n]:t,l=e.style;return s&&(""!==u||b.contains(e.ownerDocument,e)||(u=b.style(e,n)),Yt.test(u)&&Ut.test(n)&&(i=l.width,o=l.minWidth,a=l.maxWidth,l.minWidth=l.maxWidth=l.width=u,u=s.width,l.width=i,l.minWidth=o,l.maxWidth=a)),u}):o.documentElement.currentStyle&&(Rt=function(e){return e.currentStyle},Wt=function(e,n,r){var i,o,a,s=r||Rt(e),u=s?s[n]:t,l=e.style;return null==u&&l&&l[n]&&(u=l[n]),Yt.test(u)&&!zt.test(n)&&(i=l.left,o=e.runtimeStyle,a=o&&o.left,a&&(o.left=e.currentStyle.left),l.left="fontSize"===n?"1em":u,u=l.pixelLeft+"px",l.left=i,a&&(o.left=a)),""===u?"auto":u});function on(e,t,n){var r=Vt.exec(t);return r?Math.max(0,r[1]-(n||0))+(r[2]||"px"):t}function an(e,t,n,r,i){var o=n===(r?"border":"content")?4:"width"===t?1:0,a=0;for(;4>o;o+=2)"margin"===n&&(a+=b.css(e,n+Zt[o],!0,i)),r?("content"===n&&(a-=b.css(e,"padding"+Zt[o],!0,i)),"margin"!==n&&(a-=b.css(e,"border"+Zt[o]+"Width",!0,i))):(a+=b.css(e,"padding"+Zt[o],!0,i),"padding"!==n&&(a+=b.css(e,"border"+Zt[o]+"Width",!0,i)));return a}function sn(e,t,n){var r=!0,i="width"===t?e.offsetWidth:e.offsetHeight,o=Rt(e),a=b.support.boxSizing&&"border-box"===b.css(e,"boxSizing",!1,o);if(0>=i||null==i){if(i=Wt(e,t,o),(0>i||null==i)&&(i=e.style[t]),Yt.test(i))return i;r=a&&(b.support.boxSizingReliable||i===e.style[t]),i=parseFloat(i)||0}return i+an(e,t,n||(a?"border":"content"),r,o)+"px"}function un(e){var t=o,n=Gt[e];return n||(n=ln(e,t),"none"!==n&&n||(Pt=(Pt||b("<iframe frameborder='0' width='0' height='0'/>").css("cssText","display:block !important")).appendTo(t.documentElement),t=(Pt[0].contentWindow||Pt[0].contentDocument).document,t.write("<!doctype html><html><body>"),t.close(),n=ln(e,t),Pt.detach()),Gt[e]=n),n}function ln(e,t){var n=b(t.createElement(e)).appendTo(t.body),r=b.css(n[0],"display");return n.remove(),r}b.each(["height","width"],function(e,n){b.cssHooks[n]={get:function(e,r,i){return r?0===e.offsetWidth&&Xt.test(b.css(e,"display"))?b.swap(e,Qt,function(){return sn(e,n,i)}):sn(e,n,i):t},set:function(e,t,r){var i=r&&Rt(e);return on(e,t,r?an(e,n,r,b.support.boxSizing&&"border-box"===b.css(e,"boxSizing",!1,i),i):0)}}}),b.support.opacity||(b.cssHooks.opacity={get:function(e,t){return It.test((t&&e.currentStyle?e.currentStyle.filter:e.style.filter)||"")?.01*parseFloat(RegExp.$1)+"":t?"1":""},set:function(e,t){var n=e.style,r=e.currentStyle,i=b.isNumeric(t)?"alpha(opacity="+100*t+")":"",o=r&&r.filter||n.filter||"";n.zoom=1,(t>=1||""===t)&&""===b.trim(o.replace($t,""))&&n.removeAttribute&&(n.removeAttribute("filter"),""===t||r&&!r.filter)||(n.filter=$t.test(o)?o.replace($t,i):o+" "+i)}}),b(function(){b.support.reliableMarginRight||(b.cssHooks.marginRight={get:function(e,n){return n?b.swap(e,{display:"inline-block"},Wt,[e,"marginRight"]):t}}),!b.support.pixelPosition&&b.fn.position&&b.each(["top","left"],function(e,n){b.cssHooks[n]={get:function(e,r){return r?(r=Wt(e,n),Yt.test(r)?b(e).position()[n]+"px":r):t}}})}),b.expr&&b.expr.filters&&(b.expr.filters.hidden=function(e){return 0>=e.offsetWidth&&0>=e.offsetHeight||!b.support.reliableHiddenOffsets&&"none"===(e.style&&e.style.display||b.css(e,"display"))},b.expr.filters.visible=function(e){return!b.expr.filters.hidden(e)}),b.each({margin:"",padding:"",border:"Width"},function(e,t){b.cssHooks[e+t]={expand:function(n){var r=0,i={},o="string"==typeof n?n.split(" "):[n];for(;4>r;r++)i[e+Zt[r]+t]=o[r]||o[r-2]||o[0];return i}},Ut.test(e)||(b.cssHooks[e+t].set=on)});var cn=/%20/g,pn=/\[\]$/,fn=/\r?\n/g,dn=/^(?:submit|button|image|reset|file)$/i,hn=/^(?:input|select|textarea|keygen)/i;b.fn.extend({serialize:function(){return b.param(this.serializeArray())},serializeArray:function(){return this.map(function(){var e=b.prop(this,"elements");return e?b.makeArray(e):this}).filter(function(){var e=this.type;return this.name&&!b(this).is(":disabled")&&hn.test(this.nodeName)&&!dn.test(e)&&(this.checked||!Nt.test(e))}).map(function(e,t){var n=b(this).val();return null==n?null:b.isArray(n)?b.map(n,function(e){return{name:t.name,value:e.replace(fn,"\r\n")}}):{name:t.name,value:n.replace(fn,"\r\n")}}).get()}}),b.param=function(e,n){var r,i=[],o=function(e,t){t=b.isFunction(t)?t():null==t?"":t,i[i.length]=encodeURIComponent(e)+"="+encodeURIComponent(t)};if(n===t&&(n=b.ajaxSettings&&b.ajaxSettings.traditional),b.isArray(e)||e.jquery&&!b.isPlainObject(e))b.each(e,function(){o(this.name,this.value)});else for(r in e)gn(r,e[r],n,o);return i.join("&").replace(cn,"+")};function gn(e,t,n,r){var i;if(b.isArray(t))b.each(t,function(t,i){n||pn.test(e)?r(e,i):gn(e+"["+("object"==typeof i?t:"")+"]",i,n,r)});else if(n||"object"!==b.type(t))r(e,t);else for(i in t)gn(e+"["+i+"]",t[i],n,r)}b.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),function(e,t){b.fn[t]=function(e,n){return arguments.length>0?this.on(t,null,e,n):this.trigger(t)}}),b.fn.hover=function(e,t){return this.mouseenter(e).mouseleave(t||e)};var mn,yn,vn=b.now(),bn=/\?/,xn=/#.*$/,wn=/([?&])_=[^&]*/,Tn=/^(.*?):[ \t]*([^\r\n]*)\r?$/gm,Nn=/^(?:about|app|app-storage|.+-extension|file|res|widget):$/,Cn=/^(?:GET|HEAD)$/,kn=/^\/\//,En=/^([\w.+-]+:)(?:\/\/([^\/?#:]*)(?::(\d+)|)|)/,Sn=b.fn.load,An={},jn={},Dn="*/".concat("*");try{yn=a.href}catch(Ln){yn=o.createElement("a"),yn.href="",yn=yn.href}mn=En.exec(yn.toLowerCase())||[];function Hn(e){return function(t,n){"string"!=typeof t&&(n=t,t="*");var r,i=0,o=t.toLowerCase().match(w)||[];if(b.isFunction(n))while(r=o[i++])"+"===r[0]?(r=r.slice(1)||"*",(e[r]=e[r]||[]).unshift(n)):(e[r]=e[r]||[]).push(n)}}function qn(e,n,r,i){var o={},a=e===jn;function s(u){var l;return o[u]=!0,b.each(e[u]||[],function(e,u){var c=u(n,r,i);return"string"!=typeof c||a||o[c]?a?!(l=c):t:(n.dataTypes.unshift(c),s(c),!1)}),l}return s(n.dataTypes[0])||!o["*"]&&s("*")}function Mn(e,n){var r,i,o=b.ajaxSettings.flatOptions||{};for(i in n)n[i]!==t&&((o[i]?e:r||(r={}))[i]=n[i]);return r&&b.extend(!0,e,r),e}b.fn.load=function(e,n,r){if("string"!=typeof e&&Sn)return Sn.apply(this,arguments);var i,o,a,s=this,u=e.indexOf(" ");return u>=0&&(i=e.slice(u,e.length),e=e.slice(0,u)),b.isFunction(n)?(r=n,n=t):n&&"object"==typeof n&&(a="POST"),s.length>0&&b.ajax({url:e,type:a,dataType:"html",data:n}).done(function(e){o=arguments,s.html(i?b("<div>").append(b.parseHTML(e)).find(i):e)}).complete(r&&function(e,t){s.each(r,o||[e.responseText,t,e])}),this},b.each(["ajaxStart","ajaxStop","ajaxComplete","ajaxError","ajaxSuccess","ajaxSend"],function(e,t){b.fn[t]=function(e){return this.on(t,e)}}),b.each(["get","post"],function(e,n){b[n]=function(e,r,i,o){return b.isFunction(r)&&(o=o||i,i=r,r=t),b.ajax({url:e,type:n,dataType:o,data:r,success:i})}}),b.extend({active:0,lastModified:{},etag:{},ajaxSettings:{url:yn,type:"GET",isLocal:Nn.test(mn[1]),global:!0,processData:!0,async:!0,contentType:"application/x-www-form-urlencoded; charset=UTF-8",accepts:{"*":Dn,text:"text/plain",html:"text/html",xml:"application/xml, text/xml",json:"application/json, text/javascript"},contents:{xml:/xml/,html:/html/,json:/json/},responseFields:{xml:"responseXML",text:"responseText"},converters:{"* text":e.String,"text html":!0,"text json":b.parseJSON,"text xml":b.parseXML},flatOptions:{url:!0,context:!0}},ajaxSetup:function(e,t){return t?Mn(Mn(e,b.ajaxSettings),t):Mn(b.ajaxSettings,e)},ajaxPrefilter:Hn(An),ajaxTransport:Hn(jn),ajax:function(e,n){"object"==typeof e&&(n=e,e=t),n=n||{};var r,i,o,a,s,u,l,c,p=b.ajaxSetup({},n),f=p.context||p,d=p.context&&(f.nodeType||f.jquery)?b(f):b.event,h=b.Deferred(),g=b.Callbacks("once memory"),m=p.statusCode||{},y={},v={},x=0,T="canceled",N={readyState:0,getResponseHeader:function(e){var t;if(2===x){if(!c){c={};while(t=Tn.exec(a))c[t[1].toLowerCase()]=t[2]}t=c[e.toLowerCase()]}return null==t?null:t},getAllResponseHeaders:function(){return 2===x?a:null},setRequestHeader:function(e,t){var n=e.toLowerCase();return x||(e=v[n]=v[n]||e,y[e]=t),this},overrideMimeType:function(e){return x||(p.mimeType=e),this},statusCode:function(e){var t;if(e)if(2>x)for(t in e)m[t]=[m[t],e[t]];else N.always(e[N.status]);return this},abort:function(e){var t=e||T;return l&&l.abort(t),k(0,t),this}};if(h.promise(N).complete=g.add,N.success=N.done,N.error=N.fail,p.url=((e||p.url||yn)+"").replace(xn,"").replace(kn,mn[1]+"//"),p.type=n.method||n.type||p.method||p.type,p.dataTypes=b.trim(p.dataType||"*").toLowerCase().match(w)||[""],null==p.crossDomain&&(r=En.exec(p.url.toLowerCase()),p.crossDomain=!(!r||r[1]===mn[1]&&r[2]===mn[2]&&(r[3]||("http:"===r[1]?80:443))==(mn[3]||("http:"===mn[1]?80:443)))),p.data&&p.processData&&"string"!=typeof p.data&&(p.data=b.param(p.data,p.traditional)),qn(An,p,n,N),2===x)return N;u=p.global,u&&0===b.active++&&b.event.trigger("ajaxStart"),p.type=p.type.toUpperCase(),p.hasContent=!Cn.test(p.type),o=p.url,p.hasContent||(p.data&&(o=p.url+=(bn.test(o)?"&":"?")+p.data,delete p.data),p.cache===!1&&(p.url=wn.test(o)?o.replace(wn,"$1_="+vn++):o+(bn.test(o)?"&":"?")+"_="+vn++)),p.ifModified&&(b.lastModified[o]&&N.setRequestHeader("If-Modified-Since",b.lastModified[o]),b.etag[o]&&N.setRequestHeader("If-None-Match",b.etag[o])),(p.data&&p.hasContent&&p.contentType!==!1||n.contentType)&&N.setRequestHeader("Content-Type",p.contentType),N.setRequestHeader("Accept",p.dataTypes[0]&&p.accepts[p.dataTypes[0]]?p.accepts[p.dataTypes[0]]+("*"!==p.dataTypes[0]?", "+Dn+"; q=0.01":""):p.accepts["*"]);for(i in p.headers)N.setRequestHeader(i,p.headers[i]);if(p.beforeSend&&(p.beforeSend.call(f,N,p)===!1||2===x))return N.abort();T="abort";for(i in{success:1,error:1,complete:1})N[i](p[i]);if(l=qn(jn,p,n,N)){N.readyState=1,u&&d.trigger("ajaxSend",[N,p]),p.async&&p.timeout>0&&(s=setTimeout(function(){N.abort("timeout")},p.timeout));try{x=1,l.send(y,k)}catch(C){if(!(2>x))throw C;k(-1,C)}}else k(-1,"No Transport");function k(e,n,r,i){var c,y,v,w,T,C=n;2!==x&&(x=2,s&&clearTimeout(s),l=t,a=i||"",N.readyState=e>0?4:0,r&&(w=_n(p,N,r)),e>=200&&300>e||304===e?(p.ifModified&&(T=N.getResponseHeader("Last-Modified"),T&&(b.lastModified[o]=T),T=N.getResponseHeader("etag"),T&&(b.etag[o]=T)),204===e?(c=!0,C="nocontent"):304===e?(c=!0,C="notmodified"):(c=Fn(p,w),C=c.state,y=c.data,v=c.error,c=!v)):(v=C,(e||!C)&&(C="error",0>e&&(e=0))),N.status=e,N.statusText=(n||C)+"",c?h.resolveWith(f,[y,C,N]):h.rejectWith(f,[N,C,v]),N.statusCode(m),m=t,u&&d.trigger(c?"ajaxSuccess":"ajaxError",[N,p,c?y:v]),g.fireWith(f,[N,C]),u&&(d.trigger("ajaxComplete",[N,p]),--b.active||b.event.trigger("ajaxStop")))}return N},getScript:function(e,n){return b.get(e,t,n,"script")},getJSON:function(e,t,n){return b.get(e,t,n,"json")}});function _n(e,n,r){var i,o,a,s,u=e.contents,l=e.dataTypes,c=e.responseFields;for(s in c)s in r&&(n[c[s]]=r[s]);while("*"===l[0])l.shift(),o===t&&(o=e.mimeType||n.getResponseHeader("Content-Type"));if(o)for(s in u)if(u[s]&&u[s].test(o)){l.unshift(s);break}if(l[0]in r)a=l[0];else{for(s in r){if(!l[0]||e.converters[s+" "+l[0]]){a=s;break}i||(i=s)}a=a||i}return a?(a!==l[0]&&l.unshift(a),r[a]):t}function Fn(e,t){var n,r,i,o,a={},s=0,u=e.dataTypes.slice(),l=u[0];if(e.dataFilter&&(t=e.dataFilter(t,e.dataType)),u[1])for(i in e.converters)a[i.toLowerCase()]=e.converters[i];for(;r=u[++s];)if("*"!==r){if("*"!==l&&l!==r){if(i=a[l+" "+r]||a["* "+r],!i)for(n in a)if(o=n.split(" "),o[1]===r&&(i=a[l+" "+o[0]]||a["* "+o[0]])){i===!0?i=a[n]:a[n]!==!0&&(r=o[0],u.splice(s--,0,r));break}if(i!==!0)if(i&&e["throws"])t=i(t);else try{t=i(t)}catch(c){return{state:"parsererror",error:i?c:"No conversion from "+l+" to "+r}}}l=r}return{state:"success",data:t}}b.ajaxSetup({accepts:{script:"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"},contents:{script:/(?:java|ecma)script/},converters:{"text script":function(e){return b.globalEval(e),e}}}),b.ajaxPrefilter("script",function(e){e.cache===t&&(e.cache=!1),e.crossDomain&&(e.type="GET",e.global=!1)}),b.ajaxTransport("script",function(e){if(e.crossDomain){var n,r=o.head||b("head")[0]||o.documentElement;return{send:function(t,i){n=o.createElement("script"),n.async=!0,e.scriptCharset&&(n.charset=e.scriptCharset),n.src=e.url,n.onload=n.onreadystatechange=function(e,t){(t||!n.readyState||/loaded|complete/.test(n.readyState))&&(n.onload=n.onreadystatechange=null,n.parentNode&&n.parentNode.removeChild(n),n=null,t||i(200,"success"))},r.insertBefore(n,r.firstChild)},abort:function(){n&&n.onload(t,!0)}}}});var On=[],Bn=/(=)\?(?=&|$)|\?\?/;b.ajaxSetup({jsonp:"callback",jsonpCallback:function(){var e=On.pop()||b.expando+"_"+vn++;return this[e]=!0,e}}),b.ajaxPrefilter("json jsonp",function(n,r,i){var o,a,s,u=n.jsonp!==!1&&(Bn.test(n.url)?"url":"string"==typeof n.data&&!(n.contentType||"").indexOf("application/x-www-form-urlencoded")&&Bn.test(n.data)&&"data");return u||"jsonp"===n.dataTypes[0]?(o=n.jsonpCallback=b.isFunction(n.jsonpCallback)?n.jsonpCallback():n.jsonpCallback,u?n[u]=n[u].replace(Bn,"$1"+o):n.jsonp!==!1&&(n.url+=(bn.test(n.url)?"&":"?")+n.jsonp+"="+o),n.converters["script json"]=function(){return s||b.error(o+" was not called"),s[0]},n.dataTypes[0]="json",a=e[o],e[o]=function(){s=arguments},i.always(function(){e[o]=a,n[o]&&(n.jsonpCallback=r.jsonpCallback,On.push(o)),s&&b.isFunction(a)&&a(s[0]),s=a=t}),"script"):t});var Pn,Rn,Wn=0,$n=e.ActiveXObject&&function(){var e;for(e in Pn)Pn[e](t,!0)};function In(){try{return new e.XMLHttpRequest}catch(t){}}function zn(){try{return new e.ActiveXObject("Microsoft.XMLHTTP")}catch(t){}}b.ajaxSettings.xhr=e.ActiveXObject?function(){return!this.isLocal&&In()||zn()}:In,Rn=b.ajaxSettings.xhr(),b.support.cors=!!Rn&&"withCredentials"in Rn,Rn=b.support.ajax=!!Rn,Rn&&b.ajaxTransport(function(n){if(!n.crossDomain||b.support.cors){var r;return{send:function(i,o){var a,s,u=n.xhr();if(n.username?u.open(n.type,n.url,n.async,n.username,n.password):u.open(n.type,n.url,n.async),n.xhrFields)for(s in n.xhrFields)u[s]=n.xhrFields[s];n.mimeType&&u.overrideMimeType&&u.overrideMimeType(n.mimeType),n.crossDomain||i["X-Requested-With"]||(i["X-Requested-With"]="XMLHttpRequest");try{for(s in i)u.setRequestHeader(s,i[s])}catch(l){}u.send(n.hasContent&&n.data||null),r=function(e,i){var s,l,c,p;try{if(r&&(i||4===u.readyState))if(r=t,a&&(u.onreadystatechange=b.noop,$n&&delete Pn[a]),i)4!==u.readyState&&u.abort();else{p={},s=u.status,l=u.getAllResponseHeaders(),"string"==typeof u.responseText&&(p.text=u.responseText);try{c=u.statusText}catch(f){c=""}s||!n.isLocal||n.crossDomain?1223===s&&(s=204):s=p.text?200:404}}catch(d){i||o(-1,d)}p&&o(s,c,p,l)},n.async?4===u.readyState?setTimeout(r):(a=++Wn,$n&&(Pn||(Pn={},b(e).unload($n)),Pn[a]=r),u.onreadystatechange=r):r()},abort:function(){r&&r(t,!0)}}}});var Xn,Un,Vn=/^(?:toggle|show|hide)$/,Yn=RegExp("^(?:([+-])=|)("+x+")([a-z%]*)$","i"),Jn=/queueHooks$/,Gn=[nr],Qn={"*":[function(e,t){var n,r,i=this.createTween(e,t),o=Yn.exec(t),a=i.cur(),s=+a||0,u=1,l=20;if(o){if(n=+o[2],r=o[3]||(b.cssNumber[e]?"":"px"),"px"!==r&&s){s=b.css(i.elem,e,!0)||n||1;do u=u||".5",s/=u,b.style(i.elem,e,s+r);while(u!==(u=i.cur()/a)&&1!==u&&--l)}i.unit=r,i.start=s,i.end=o[1]?s+(o[1]+1)*n:n}return i}]};function Kn(){return setTimeout(function(){Xn=t}),Xn=b.now()}function Zn(e,t){b.each(t,function(t,n){var r=(Qn[t]||[]).concat(Qn["*"]),i=0,o=r.length;for(;o>i;i++)if(r[i].call(e,t,n))return})}function er(e,t,n){var r,i,o=0,a=Gn.length,s=b.Deferred().always(function(){delete u.elem}),u=function(){if(i)return!1;var t=Xn||Kn(),n=Math.max(0,l.startTime+l.duration-t),r=n/l.duration||0,o=1-r,a=0,u=l.tweens.length;for(;u>a;a++)l.tweens[a].run(o);return s.notifyWith(e,[l,o,n]),1>o&&u?n:(s.resolveWith(e,[l]),!1)},l=s.promise({elem:e,props:b.extend({},t),opts:b.extend(!0,{specialEasing:{}},n),originalProperties:t,originalOptions:n,startTime:Xn||Kn(),duration:n.duration,tweens:[],createTween:function(t,n){var r=b.Tween(e,l.opts,t,n,l.opts.specialEasing[t]||l.opts.easing);return l.tweens.push(r),r},stop:function(t){var n=0,r=t?l.tweens.length:0;if(i)return this;for(i=!0;r>n;n++)l.tweens[n].run(1);return t?s.resolveWith(e,[l,t]):s.rejectWith(e,[l,t]),this}}),c=l.props;for(tr(c,l.opts.specialEasing);a>o;o++)if(r=Gn[o].call(l,e,c,l.opts))return r;return Zn(l,c),b.isFunction(l.opts.start)&&l.opts.start.call(e,l),b.fx.timer(b.extend(u,{elem:e,anim:l,queue:l.opts.queue})),l.progress(l.opts.progress).done(l.opts.done,l.opts.complete).fail(l.opts.fail).always(l.opts.always)}function tr(e,t){var n,r,i,o,a;for(i in e)if(r=b.camelCase(i),o=t[r],n=e[i],b.isArray(n)&&(o=n[1],n=e[i]=n[0]),i!==r&&(e[r]=n,delete e[i]),a=b.cssHooks[r],a&&"expand"in a){n=a.expand(n),delete e[r];for(i in n)i in e||(e[i]=n[i],t[i]=o)}else t[r]=o}b.Animation=b.extend(er,{tweener:function(e,t){b.isFunction(e)?(t=e,e=["*"]):e=e.split(" ");var n,r=0,i=e.length;for(;i>r;r++)n=e[r],Qn[n]=Qn[n]||[],Qn[n].unshift(t)},prefilter:function(e,t){t?Gn.unshift(e):Gn.push(e)}});function nr(e,t,n){var r,i,o,a,s,u,l,c,p,f=this,d=e.style,h={},g=[],m=e.nodeType&&nn(e);n.queue||(c=b._queueHooks(e,"fx"),null==c.unqueued&&(c.unqueued=0,p=c.empty.fire,c.empty.fire=function(){c.unqueued||p()}),c.unqueued++,f.always(function(){f.always(function(){c.unqueued--,b.queue(e,"fx").length||c.empty.fire()})})),1===e.nodeType&&("height"in t||"width"in t)&&(n.overflow=[d.overflow,d.overflowX,d.overflowY],"inline"===b.css(e,"display")&&"none"===b.css(e,"float")&&(b.support.inlineBlockNeedsLayout&&"inline"!==un(e.nodeName)?d.zoom=1:d.display="inline-block")),n.overflow&&(d.overflow="hidden",b.support.shrinkWrapBlocks||f.always(function(){d.overflow=n.overflow[0],d.overflowX=n.overflow[1],d.overflowY=n.overflow[2]}));for(i in t)if(a=t[i],Vn.exec(a)){if(delete t[i],u=u||"toggle"===a,a===(m?"hide":"show"))continue;g.push(i)}if(o=g.length){s=b._data(e,"fxshow")||b._data(e,"fxshow",{}),"hidden"in s&&(m=s.hidden),u&&(s.hidden=!m),m?b(e).show():f.done(function(){b(e).hide()}),f.done(function(){var t;b._removeData(e,"fxshow");for(t in h)b.style(e,t,h[t])});for(i=0;o>i;i++)r=g[i],l=f.createTween(r,m?s[r]:0),h[r]=s[r]||b.style(e,r),r in s||(s[r]=l.start,m&&(l.end=l.start,l.start="width"===r||"height"===r?1:0))}}function rr(e,t,n,r,i){return new rr.prototype.init(e,t,n,r,i)}b.Tween=rr,rr.prototype={constructor:rr,init:function(e,t,n,r,i,o){this.elem=e,this.prop=n,this.easing=i||"swing",this.options=t,this.start=this.now=this.cur(),this.end=r,this.unit=o||(b.cssNumber[n]?"":"px")},cur:function(){var e=rr.propHooks[this.prop];return e&&e.get?e.get(this):rr.propHooks._default.get(this)},run:function(e){var t,n=rr.propHooks[this.prop];return this.pos=t=this.options.duration?b.easing[this.easing](e,this.options.duration*e,0,1,this.options.duration):e,this.now=(this.end-this.start)*t+this.start,this.options.step&&this.options.step.call(this.elem,this.now,this),n&&n.set?n.set(this):rr.propHooks._default.set(this),this}},rr.prototype.init.prototype=rr.prototype,rr.propHooks={_default:{get:function(e){var t;return null==e.elem[e.prop]||e.elem.style&&null!=e.elem.style[e.prop]?(t=b.css(e.elem,e.prop,""),t&&"auto"!==t?t:0):e.elem[e.prop]},set:function(e){b.fx.step[e.prop]?b.fx.step[e.prop](e):e.elem.style&&(null!=e.elem.style[b.cssProps[e.prop]]||b.cssHooks[e.prop])?b.style(e.elem,e.prop,e.now+e.unit):e.elem[e.prop]=e.now}}},rr.propHooks.scrollTop=rr.propHooks.scrollLeft={set:function(e){e.elem.nodeType&&e.elem.parentNode&&(e.elem[e.prop]=e.now)}},b.each(["toggle","show","hide"],function(e,t){var n=b.fn[t];b.fn[t]=function(e,r,i){return null==e||"boolean"==typeof e?n.apply(this,arguments):this.animate(ir(t,!0),e,r,i)}}),b.fn.extend({fadeTo:function(e,t,n,r){return this.filter(nn).css("opacity",0).show().end().animate({opacity:t},e,n,r)},animate:function(e,t,n,r){var i=b.isEmptyObject(e),o=b.speed(t,n,r),a=function(){var t=er(this,b.extend({},e),o);a.finish=function(){t.stop(!0)},(i||b._data(this,"finish"))&&t.stop(!0)};return a.finish=a,i||o.queue===!1?this.each(a):this.queue(o.queue,a)},stop:function(e,n,r){var i=function(e){var t=e.stop;delete e.stop,t(r)};return"string"!=typeof e&&(r=n,n=e,e=t),n&&e!==!1&&this.queue(e||"fx",[]),this.each(function(){var t=!0,n=null!=e&&e+"queueHooks",o=b.timers,a=b._data(this);if(n)a[n]&&a[n].stop&&i(a[n]);else for(n in a)a[n]&&a[n].stop&&Jn.test(n)&&i(a[n]);for(n=o.length;n--;)o[n].elem!==this||null!=e&&o[n].queue!==e||(o[n].anim.stop(r),t=!1,o.splice(n,1));(t||!r)&&b.dequeue(this,e)})},finish:function(e){return e!==!1&&(e=e||"fx"),this.each(function(){var t,n=b._data(this),r=n[e+"queue"],i=n[e+"queueHooks"],o=b.timers,a=r?r.length:0;for(n.finish=!0,b.queue(this,e,[]),i&&i.cur&&i.cur.finish&&i.cur.finish.call(this),t=o.length;t--;)o[t].elem===this&&o[t].queue===e&&(o[t].anim.stop(!0),o.splice(t,1));for(t=0;a>t;t++)r[t]&&r[t].finish&&r[t].finish.call(this);delete n.finish})}});function ir(e,t){var n,r={height:e},i=0;for(t=t?1:0;4>i;i+=2-t)n=Zt[i],r["margin"+n]=r["padding"+n]=e;return t&&(r.opacity=r.width=e),r}b.each({slideDown:ir("show"),slideUp:ir("hide"),slideToggle:ir("toggle"),fadeIn:{opacity:"show"},fadeOut:{opacity:"hide"},fadeToggle:{opacity:"toggle"}},function(e,t){b.fn[e]=function(e,n,r){return this.animate(t,e,n,r)}}),b.speed=function(e,t,n){var r=e&&"object"==typeof e?b.extend({},e):{complete:n||!n&&t||b.isFunction(e)&&e,duration:e,easing:n&&t||t&&!b.isFunction(t)&&t};return r.duration=b.fx.off?0:"number"==typeof r.duration?r.duration:r.duration in b.fx.speeds?b.fx.speeds[r.duration]:b.fx.speeds._default,(null==r.queue||r.queue===!0)&&(r.queue="fx"),r.old=r.complete,r.complete=function(){b.isFunction(r.old)&&r.old.call(this),r.queue&&b.dequeue(this,r.queue)},r},b.easing={linear:function(e){return e},swing:function(e){return.5-Math.cos(e*Math.PI)/2}},b.timers=[],b.fx=rr.prototype.init,b.fx.tick=function(){var e,n=b.timers,r=0;for(Xn=b.now();n.length>r;r++)e=n[r],e()||n[r]!==e||n.splice(r--,1);n.length||b.fx.stop(),Xn=t},b.fx.timer=function(e){e()&&b.timers.push(e)&&b.fx.start()},b.fx.interval=13,b.fx.start=function(){Un||(Un=setInterval(b.fx.tick,b.fx.interval))},b.fx.stop=function(){clearInterval(Un),Un=null},b.fx.speeds={slow:600,fast:200,_default:400},b.fx.step={},b.expr&&b.expr.filters&&(b.expr.filters.animated=function(e){return b.grep(b.timers,function(t){return e===t.elem}).length}),b.fn.offset=function(e){if(arguments.length)return e===t?this:this.each(function(t){b.offset.setOffset(this,e,t)});var n,r,o={top:0,left:0},a=this[0],s=a&&a.ownerDocument;if(s)return n=s.documentElement,b.contains(n,a)?(typeof a.getBoundingClientRect!==i&&(o=a.getBoundingClientRect()),r=or(s),{top:o.top+(r.pageYOffset||n.scrollTop)-(n.clientTop||0),left:o.left+(r.pageXOffset||n.scrollLeft)-(n.clientLeft||0)}):o},b.offset={setOffset:function(e,t,n){var r=b.css(e,"position");"static"===r&&(e.style.position="relative");var i=b(e),o=i.offset(),a=b.css(e,"top"),s=b.css(e,"left"),u=("absolute"===r||"fixed"===r)&&b.inArray("auto",[a,s])>-1,l={},c={},p,f;u?(c=i.position(),p=c.top,f=c.left):(p=parseFloat(a)||0,f=parseFloat(s)||0),b.isFunction(t)&&(t=t.call(e,n,o)),null!=t.top&&(l.top=t.top-o.top+p),null!=t.left&&(l.left=t.left-o.left+f),"using"in t?t.using.call(e,l):i.css(l)}},b.fn.extend({position:function(){if(this[0]){var e,t,n={top:0,left:0},r=this[0];return"fixed"===b.css(r,"position")?t=r.getBoundingClientRect():(e=this.offsetParent(),t=this.offset(),b.nodeName(e[0],"html")||(n=e.offset()),n.top+=b.css(e[0],"borderTopWidth",!0),n.left+=b.css(e[0],"borderLeftWidth",!0)),{top:t.top-n.top-b.css(r,"marginTop",!0),left:t.left-n.left-b.css(r,"marginLeft",!0)}}},offsetParent:function(){return this.map(function(){var e=this.offsetParent||o.documentElement;while(e&&!b.nodeName(e,"html")&&"static"===b.css(e,"position"))e=e.offsetParent;return e||o.documentElement})}}),b.each({scrollLeft:"pageXOffset",scrollTop:"pageYOffset"},function(e,n){var r=/Y/.test(n);b.fn[e]=function(i){return b.access(this,function(e,i,o){var a=or(e);return o===t?a?n in a?a[n]:a.document.documentElement[i]:e[i]:(a?a.scrollTo(r?b(a).scrollLeft():o,r?o:b(a).scrollTop()):e[i]=o,t)},e,i,arguments.length,null)}});function or(e){return b.isWindow(e)?e:9===e.nodeType?e.defaultView||e.parentWindow:!1}b.each({Height:"height",Width:"width"},function(e,n){b.each({padding:"inner"+e,content:n,"":"outer"+e},function(r,i){b.fn[i]=function(i,o){var a=arguments.length&&(r||"boolean"!=typeof i),s=r||(i===!0||o===!0?"margin":"border");return b.access(this,function(n,r,i){var o;return b.isWindow(n)?n.document.documentElement["client"+e]:9===n.nodeType?(o=n.documentElement,Math.max(n.body["scroll"+e],o["scroll"+e],n.body["offset"+e],o["offset"+e],o["client"+e])):i===t?b.css(n,r,s):b.style(n,r,i,s)},n,a?i:t,a,null)}})}),e.jQuery=e.$=b,"function"==typeof define&&define.amd&&define.amd.jQuery&&define("jquery",[],function(){return b})})(window);
/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD
		define(['jquery'], factory);
	} else if (typeof exports === 'object') {
		// CommonJS
		factory(require('jquery'));
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function ($) {

	var pluses = /\+/g;

	function encode(s) {
		return config.raw ? s : encodeURIComponent(s);
	}

	function decode(s) {
		return config.raw ? s : decodeURIComponent(s);
	}

	function stringifyCookieValue(value) {
		return encode(config.json ? JSON.stringify(value) : String(value));
	}

	function parseCookieValue(s) {
		if (s.indexOf('"') === 0) {
			// This is a quoted cookie as according to RFC2068, unescape...
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
		}

		try {
			// Replace server-side written pluses with spaces.
			// If we can't decode the cookie, ignore it, it's unusable.
			// If we can't parse the cookie, ignore it, it's unusable.
			s = decodeURIComponent(s.replace(pluses, ' '));
			return config.json ? JSON.parse(s) : s;
		} catch(e) {}
	}

	function read(s, converter) {
		var value = config.raw ? s : parseCookieValue(s);
		return $.isFunction(converter) ? converter(value) : value;
	}

	var config = $.cookie = function (key, value, options) {

		// Write

		if (value !== undefined && !$.isFunction(value)) {
			options = $.extend({}, config.defaults, options);

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setTime(+t + days * 864e+5);
			}

			return (document.cookie = [
				encode(key), '=', stringifyCookieValue(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path    ? '; path=' + options.path : '',
				options.domain  ? '; domain=' + options.domain : '',
				options.secure  ? '; secure' : ''
			].join(''));
		}

		// Read

		var result = key ? undefined : {};

		// To prevent the for loop in the first place assign an empty array
		// in case there are no cookies at all. Also prevents odd result when
		// calling $.cookie().
		var cookies = document.cookie ? document.cookie.split('; ') : [];

		for (var i = 0, l = cookies.length; i < l; i++) {
			var parts = cookies[i].split('=');
			var name = decode(parts.shift());
			var cookie = parts.join('=');

			if (key && key === name) {
				// If second argument (value) is a function it's a converter...
				result = read(cookie, value);
				break;
			}

			// Prevent storing a cookie that we couldn't decode.
			if (!key && (cookie = read(cookie)) !== undefined) {
				result[name] = cookie;
			}
		}

		return result;
	};

	config.defaults = {};

	$.removeCookie = function (key, options) {
		if ($.cookie(key) === undefined) {
			return false;
		}

		// Must not alter options, thus extending a fresh object...
		$.cookie(key, '', $.extend({}, options, { expires: -1 }));
		return !$.cookie(key);
	};

}));

/**
 * jQuery.marquee - scrolling text like old marquee element
 * @author Aamir Afridi - aamirafridi(at)gmail(dot)com / http://aamirafridi.com/jquery/jquery-marquee-plugin
 */
(function(f){f.fn.marquee=function(x){return this.each(function(){var a=f.extend({},f.fn.marquee.defaults,x),b=f(this),c,h,t,u,k,e=3,y="animation-play-state",n=!1,E=function(a,b,c){for(var e=["webkit","moz","MS","o",""],d=0;d<e.length;d++)e[d]||(b=b.toLowerCase()),a.addEventListener(e[d]+b,c,!1)},F=function(a){var b=[],c;for(c in a)a.hasOwnProperty(c)&&b.push(c+":"+a[c]);b.push();return"{"+b.join(",")+"}"},p={pause:function(){n&&a.allowCss3Support?c.css(y,"paused"):f.fn.pause&&c.pause();b.data("runningStatus",
"paused");b.trigger("paused")},resume:function(){n&&a.allowCss3Support?c.css(y,"running"):f.fn.resume&&c.resume();b.data("runningStatus","resumed");b.trigger("resumed")},toggle:function(){p["resumed"==b.data("runningStatus")?"pause":"resume"]()},destroy:function(){clearTimeout(b.timer);b.find("*").andSelf().unbind();b.html(b.find(".js-marquee:first").html())}};if("string"===typeof x)f.isFunction(p[x])&&(c||(c=b.find(".js-marquee-wrapper")),!0===b.data("css3AnimationIsSupported")&&(n=!0),p[x]());else{var v;
f.each(a,function(c,d){v=b.attr("data-"+c);if("undefined"!==typeof v){switch(v){case "true":v=!0;break;case "false":v=!1}a[c]=v}});a.speed&&(a.duration=a.speed*parseInt(b.width(),10));u="up"==a.direction||"down"==a.direction;a.gap=a.duplicated?parseInt(a.gap):0;b.wrapInner('<div class="js-marquee"></div>');var l=b.find(".js-marquee").css({"margin-right":a.gap,"float":"left"});a.duplicated&&l.clone(!0).appendTo(b);b.wrapInner('<div style="width:100000px" class="js-marquee-wrapper"></div>');c=b.find(".js-marquee-wrapper");
if(u){var m=b.height();c.removeAttr("style");b.height(m);b.find(".js-marquee").css({"float":"none","margin-bottom":a.gap,"margin-right":0});a.duplicated&&b.find(".js-marquee:last").css({"margin-bottom":0});var q=b.find(".js-marquee:first").height()+a.gap;a.startVisible&&!a.duplicated?(a._completeDuration=(parseInt(q,10)+parseInt(m,10))/parseInt(m,10)*a.duration,a.duration*=parseInt(q,10)/parseInt(m,10)):a.duration*=(parseInt(q,10)+parseInt(m,10))/parseInt(m,10)}else k=b.find(".js-marquee:first").width()+
a.gap,h=b.width(),a.startVisible&&!a.duplicated?(a._completeDuration=(parseInt(k,10)+parseInt(h,10))/parseInt(h,10)*a.duration,a.duration*=parseInt(k,10)/parseInt(h,10)):a.duration*=(parseInt(k,10)+parseInt(h,10))/parseInt(h,10);a.duplicated&&(a.duration/=2);if(a.allowCss3Support){var l=document.body||document.createElement("div"),g="marqueeAnimation-"+Math.floor(1E7*Math.random()),A=["Webkit","Moz","O","ms","Khtml"],B="animation",d="",r="";l.style.animation&&(r="@keyframes "+g+" ",n=!0);if(!1===
n)for(var z=0;z<A.length;z++)if(void 0!==l.style[A[z]+"AnimationName"]){l="-"+A[z].toLowerCase()+"-";B=l+B;y=l+y;r="@"+l+"keyframes "+g+" ";n=!0;break}n&&(d=g+" "+a.duration/1E3+"s "+a.delayBeforeStart/1E3+"s infinite "+a.css3easing,b.data("css3AnimationIsSupported",!0))}var C=function(){c.css("margin-top","up"==a.direction?m+"px":"-"+q+"px")},D=function(){c.css("margin-left","left"==a.direction?h+"px":"-"+k+"px")};a.duplicated?(u?a.startVisible?c.css("margin-top",0):c.css("margin-top","up"==a.direction?
m+"px":"-"+(2*q-a.gap)+"px"):a.startVisible?c.css("margin-left",0):c.css("margin-left","left"==a.direction?h+"px":"-"+(2*k-a.gap)+"px"),a.startVisible||(e=1)):a.startVisible?e=2:u?C():D();var w=function(){a.duplicated&&(1===e?(a._originalDuration=a.duration,a.duration=u?"up"==a.direction?a.duration+m/(q/a.duration):2*a.duration:"left"==a.direction?a.duration+h/(k/a.duration):2*a.duration,d&&(d=g+" "+a.duration/1E3+"s "+a.delayBeforeStart/1E3+"s "+a.css3easing),e++):2===e&&(a.duration=a._originalDuration,
d&&(g+="0",r=f.trim(r)+"0 ",d=g+" "+a.duration/1E3+"s 0s infinite "+a.css3easing),e++));u?a.duplicated?(2<e&&c.css("margin-top","up"==a.direction?0:"-"+q+"px"),t={"margin-top":"up"==a.direction?"-"+q+"px":0}):a.startVisible?2===e?(d&&(d=g+" "+a.duration/1E3+"s "+a.delayBeforeStart/1E3+"s "+a.css3easing),t={"margin-top":"up"==a.direction?"-"+q+"px":m+"px"},e++):3===e&&(a.duration=a._completeDuration,d&&(g+="0",r=f.trim(r)+"0 ",d=g+" "+a.duration/1E3+"s 0s infinite "+a.css3easing),C()):(C(),t={"margin-top":"up"==
a.direction?"-"+c.height()+"px":m+"px"}):a.duplicated?(2<e&&c.css("margin-left","left"==a.direction?0:"-"+k+"px"),t={"margin-left":"left"==a.direction?"-"+k+"px":0}):a.startVisible?2===e?(d&&(d=g+" "+a.duration/1E3+"s "+a.delayBeforeStart/1E3+"s "+a.css3easing),t={"margin-left":"left"==a.direction?"-"+k+"px":h+"px"},e++):3===e&&(a.duration=a._completeDuration,d&&(g+="0",r=f.trim(r)+"0 ",d=g+" "+a.duration/1E3+"s 0s infinite "+a.css3easing),D()):(D(),t={"margin-left":"left"==a.direction?"-"+k+"px":
h+"px"});b.trigger("beforeStarting");if(n){c.css(B,d);var l=r+" { 100%  "+F(t)+"}",p=c.find("style");0!==p.length?p.filter(":last").html(l):c.append("<style>"+l+"</style>");E(c[0],"AnimationIteration",function(){b.trigger("finished")});E(c[0],"AnimationEnd",function(){w();b.trigger("finished")})}else c.animate(t,a.duration,a.easing,function(){b.trigger("finished");a.pauseOnCycle?b.timer=setTimeout(w,a.delayBeforeStart):w()});b.data("runningStatus","resumed")};b.bind("pause",p.pause);b.bind("resume",
p.resume);a.pauseOnHover&&b.bind("mouseenter mouseleave",p.toggle);n&&a.allowCss3Support?w():b.timer=setTimeout(w,a.delayBeforeStart)}})};f.fn.marquee.defaults={allowCss3Support:!0,css3easing:"linear",easing:"linear",delayBeforeStart:1E3,direction:"left",duplicated:!1,duration:5E3,gap:20,pauseOnCycle:!1,pauseOnHover:!1,startVisible:!1}})(jQuery);

/*!
 * jquery-powerFloat.js
 * jQuery 万能浮动层插件
 * http://www.zhangxinxu.com/wordpress/?p=1328
 * © by zhangxinxu  
 * 2010-12-06 v1.0.0	插件编写，初步调试
 * 2010-12-30 v1.0.1	限定尖角字符字体，避免受浏览器自定义字体干扰
 * 2011-01-03 v1.1.0	修复连续获得焦点显示后又隐藏的bug
 						修复图片加载正则判断不准确的问题
 * 2011-02-15 v1.1.1	关于居中对齐位置判断的特殊处理
 * 2011-04-15 v1.2.0	修复浮动层含有过高select框在IE下点击会隐藏浮动层的问题，同时优化事件绑定			
 * 2011-09-13 v1.3.0 	修复两个菜单hover时间间隔过短隐藏回调不执行的问题
 * 2012-01-13 v1.4.0	去除ajax加载的存储
                    	修复之前按照ajax地址后缀判断是否图片的问题
						修复一些脚本运行出错
						修复hover延时显示时，元素没有显示但鼠标移出依然触发隐藏回调的问题
 * 2012-02-27 v1.5.0	为无id容器创建id逻辑使用错误的问题
 						修复无事件浮动出现时同页面点击空白区域浮动层不隐藏的问题
						修复点击与hover并存时特定时候o.trigger报为null错误的问题
 * 2012-03-29 v1.5.1	修复连续hover时候后面一个不触发显示的问题
 * 2012-05-02 v1.5.2	点击事件 浮动框再次点击隐藏的问题修复
 * 2012-11-02 v1.6.0	兼容jQuery 1.8.2
 * 2012-01-28 v1.6.1	target参数支持funtion类型，以实现类似动态Ajax地址功能
 */
(function(a){a.fn.powerFloat=function(d){return a(this).each(function(){var f=a.extend({},b,d||{});var g=function(i,h){if(c.target&&c.target.css("display")!=="none"){c.targetHide()}c.s=i;c.trigger=h},e;switch(f.eventType){case"hover":a(this).hover(function(){if(c.timerHold){c.flagDisplay=true}var h=parseInt(f.showDelay,10);g(f,a(this));if(h){if(e){clearTimeout(e)}e=setTimeout(function(){c.targetGet.call(c)},h)}else{c.targetGet()}},function(){if(e){clearTimeout(e)}if(c.timerHold){clearTimeout(c.timerHold)}c.flagDisplay=false;c.targetHold()});if(f.hoverFollow){a(this).mousemove(function(h){c.cacheData.left=h.pageX;c.cacheData.top=h.pageY;c.targetGet.call(c);return false})}break;case"click":a(this).click(function(h){if(c.display&&c.trigger&&h.target===c.trigger.get(0)){c.flagDisplay=false;c.displayDetect()}else{g(f,a(this));c.targetGet();if(!a(document).data("mouseupBind")){a(document).bind("mouseup",function(k){var i=false;if(c.trigger){var j=c.target.attr("id");if(!j){j="R_"+Math.random();c.target.attr("id",j)}a(k.target).parents().each(function(){if(a(this).attr("id")===j){i=true}});if(f.eventType==="click"&&c.display&&k.target!=c.trigger.get(0)&&!i){c.flagDisplay=false;c.displayDetect()}}return false}).data("mouseupBind",true)}}});break;case"focus":a(this).focus(function(){var h=a(this);setTimeout(function(){g(f,h);c.targetGet()},200)}).blur(function(){c.flagDisplay=false;setTimeout(function(){c.displayDetect()},190)});break;default:g(f,a(this));c.targetGet();a(document).unbind("mouseup").data("mouseupBind",false)}})};var c={targetGet:function(){if(!this.trigger){return this}var h=this.trigger.attr(this.s.targetAttr),g=typeof this.s.target=="function"?this.s.target.call(this.trigger):this.s.target;switch(this.s.targetMode){case"common":if(g){var i=typeof(g);if(i==="object"){if(g.size()){c.target=g.eq(0)}}else{if(i==="string"){if(a(g).size()){c.target=a(g).eq(0)}}}}else{if(h&&a("#"+h).size()){c.target=a("#"+h)}}if(c.target){c.targetShow()}else{return this}break;case"ajax":var d=g||h;this.targetProtect=false;if(!d){return}if(!c.cacheData[d]){c.loading()}var f=new Image();f.onload=function(){var m=f.width,q=f.height;var p=a(window).width(),s=a(window).height();var r=m/q,o=p/s;if(r>o){if(m>p/2){m=p/2;q=m/r}}else{if(q>s/2){q=s/2;m=q*r}}var n='<img class="float_ajax_image" src="'+d+'" width="'+m+'" height = "'+q+'" />';c.cacheData[d]=true;c.target=a(n);c.targetShow()};f.onerror=function(){if(/(\.jpg|\.png|\.gif|\.bmp|\.jpeg)$/i.test(d)){c.target=a('<div class="float_ajax_error">图片加载失败。</div>');c.targetShow()}else{a.ajax({url:d,success:function(m){if(typeof(m)==="string"){c.cacheData[d]=true;c.target=a('<div class="float_ajax_data">'+m+"</div>");c.targetShow()}},error:function(){c.target=a('<div class="float_ajax_error">数据没有加载成功。</div>');c.targetShow()}})}};f.src=d;break;case"list":var k='<ul class="float_list_ul">',j;if(a.isArray(g)&&(j=g.length)){a.each(g,function(n,p){var o="",r="",q,m;if(n===0){r=' class="float_list_li_first"'}if(n===j-1){r=' class="float_list_li_last"'}if(typeof(p)==="object"&&(q=p.text.toString())){if(m=(p.href||"javascript:")){o='<a href="'+m+'" class="float_list_a">'+q+"</a>"}else{o=q}}else{if(typeof(p)==="string"&&p){o=p}}if(o){k+="<li"+r+">"+o+"</li>"}})}else{k+='<li class="float_list_null">列表无数据。</li>'}k+="</ul>";c.target=a(k);this.targetProtect=false;c.targetShow();break;case"remind":var l=g||h;this.targetProtect=false;if(typeof(l)==="string"){c.target=a("<span>"+l+"</span>");c.targetShow()}break;default:var e=g||h,i=typeof(e);if(e){if(i==="string"){if(/^.[^:#\[\.,]*$/.test(e)){if(a(e).size()){c.target=a(e).eq(0);this.targetProtect=true}else{if(a("#"+e).size()){c.target=a("#"+e).eq(0);this.targetProtect=true}else{c.target=a("<div>"+e+"</div>");this.targetProtect=false}}}else{c.target=a("<div>"+e+"</div>");this.targetProtect=false}c.targetShow()}else{if(i==="object"){if(!a.isArray(e)&&e.size()){c.target=e.eq(0);this.targetProtect=true;c.targetShow()}}}}}return this},container:function(){var d=this.s.container,e=this.s.targetMode||"mode";if(e==="ajax"||e==="remind"){this.s.sharpAngle=true}else{this.s.sharpAngle=false}if(this.s.reverseSharp){this.s.sharpAngle=!this.s.sharpAngle}if(e!=="common"){if(d===null){d="plugin"}if(d==="plugin"){if(!a("#floatBox_"+e).size()){a('<div id="floatBox_'+e+'" class="float_'+e+'_box"></div>').appendTo(a("body")).hide()}d=a("#floatBox_"+e)}if(d&&typeof(d)!=="string"&&d.size()){if(this.targetProtect){c.target.show().css("position","static")}c.target=d.empty().append(c.target)}}return this},setWidth:function(){var d=this.s.width;if(d==="auto"){if(this.target.get(0).style.width){this.target.css("width","auto")}}else{if(d==="inherit"){this.target.width(this.trigger.width())}else{this.target.css("width",d)}}return this},position:function(){if(!this.trigger||!this.target){return this}var h,x=0,k=0,m=0,y=0,s,o,e,E,u,q,f=this.target.data("height"),C=this.target.data("width"),r=a(window).scrollTop(),B=parseInt(this.s.offsets.x,10)||0,A=parseInt(this.s.offsets.y,10)||0,w=this.cacheData;if(!f){f=this.target.outerHeight();if(this.s.hoverFollow){this.target.data("height",f)}}if(!C){C=this.target.outerWidth();if(this.s.hoverFollow){this.target.data("width",C)}}h=this.trigger.offset();x=this.trigger.outerHeight();k=this.trigger.outerWidth();s=h.left;o=h.top;var l=function(){if(s<0){s=0}else{if(s+x>a(window).width()){s=a(window).width()-k}}},i=function(){if(o<0){o=0}else{if(o+x>a(document).height()){o=a(document).height()-x}}};if(this.s.hoverFollow&&w.left&&w.top){if(this.s.hoverFollow==="x"){s=w.left;l()}else{if(this.s.hoverFollow==="y"){o=w.top;i()}else{s=w.left;o=w.top;l();i()}}}var g=["4-1","1-4","5-7","2-3","2-1","6-8","3-4","4-3","8-6","1-2","7-5","3-2"],v=this.s.position,d=false,j;a.each(g,function(F,G){if(G===v){d=true;return}});if(!d){v="4-1"}var D=function(F){var G="bottom";switch(F){case"1-4":case"5-7":case"2-3":G="top";break;case"2-1":case"6-8":case"3-4":G="right";break;case"1-2":case"8-6":case"4-3":G="left";break;case"4-1":case"7-5":case"3-2":G="bottom";break}return G};var n=function(F){if(F==="5-7"||F==="6-8"||F==="8-6"||F==="7-5"){return true}return false};var t=function(H){var I=0,F=0,G=(c.s.sharpAngle&&c.corner)?true:false;if(H==="right"){F=s+k+C+B;if(G){F+=c.corner.width()}if(F>a(window).width()){return false}}else{if(H==="bottom"){I=o+x+f+A;if(G){I+=c.corner.height()}if(I>r+a(window).height()){return false}}else{if(H==="top"){I=f+A;if(G){I+=c.corner.height()}if(I>o-r){return false}}else{if(H==="left"){F=C+B;if(G){F+=c.corner.width()}if(F>s){return false}}}}}return true};j=D(v);if(this.s.sharpAngle){this.createSharp(j)}if(this.s.edgeAdjust){if(t(j)){(function(){if(n(v)){return}var G={top:{right:"2-3",left:"1-4"},right:{top:"2-1",bottom:"3-4"},bottom:{right:"3-2",left:"4-1"},left:{top:"1-2",bottom:"4-3"}};var H=G[j],F;if(H){for(F in H){if(!t(F)){v=H[F]}}}})()}else{(function(){if(n(v)){var G={"5-7":"7-5","7-5":"5-7","6-8":"8-6","8-6":"6-8"};v=G[v]}else{var H={top:{left:"3-2",right:"4-1"},right:{bottom:"1-2",top:"4-3"},bottom:{left:"2-3",right:"1-4"},left:{bottom:"2-1",top:"3-4"}};var I=H[j],F=[];for(name in I){F.push(name)}if(t(F[0])||!t(F[1])){v=I[F[0]]}else{v=I[F[1]]}}})()}}var z=D(v),p=v.split("-")[0];if(this.s.sharpAngle){this.createSharp(z);m=this.corner.width(),y=this.corner.height()}if(this.s.hoverFollow){if(this.s.hoverFollow==="x"){e=s+B;if(p==="1"||p==="8"||p==="4"){e=s-(C-k)/2+B}else{e=s-(C-k)+B}if(p==="1"||p==="5"||p==="2"){E=o-A-f-y;q=o-y-A-1}else{E=o+x+A+y;q=o+x+A+1}u=h.left-(m-k)/2}else{if(this.s.hoverFollow==="y"){if(p==="1"||p==="5"||p==="2"){E=o-(f-x)/2+A}else{E=o-(f-x)+A}if(p==="1"||p==="8"||p==="4"){e=s-C-B-m;u=s-m-B-1}else{e=s+k-B+m;u=s+k+B+1}q=h.top-(y-x)/2}else{e=s+B;E=o+A}}}else{switch(z){case"top":E=o-A-f-y;if(p=="1"){e=s-B}else{if(p==="5"){e=s-(C-k)/2-B}else{e=s-(C-k)-B}}q=o-y-A-1;u=s-(m-k)/2;break;case"right":e=s+k+B+m;if(p=="2"){E=o+A}else{if(p==="6"){E=o-(f-x)/2+A}else{E=o-(f-x)+A}}u=s+k+B+1;q=o-(y-x)/2;break;case"bottom":E=o+x+A+y;if(p=="4"){e=s+B}else{if(p==="7"){e=s-(C-k)/2+B}else{e=s-(C-k)+B}}q=o+x+A+1;u=s-(m-k)/2;break;case"left":e=s-C-B-m;if(p=="2"){E=o-A}else{if(p==="6"){E=o-(C-k)/2-A}else{E=o-(f-x)-A}}u=e+m;q=o-(C-m)/2;break}}if(y&&m&&this.corner){this.corner.css({left:u,top:q,zIndex:this.s.zIndex+1})}this.target.css({position:"absolute",left:e,top:E,zIndex:this.s.zIndex});return this},createSharp:function(g){var j,k,f="",d="";var i={left:"right",right:"left",bottom:"top",top:"bottom"},e=i[g]||"top";if(this.target){j=this.target.css("background-color");if(parseInt(this.target.css("border-"+e+"-width"))>0){k=this.target.css("border-"+e+"-color")}if(k&&k!=="transparent"){f='style="color:'+k+';"'}else{f='style="display:none;"'}if(j&&j!=="transparent"){d='style="color:'+j+';"'}else{d='style="display:none;"'}}var h='<div id="floatCorner_'+g+'" class="float_corner float_corner_'+g+'"><span class="corner corner_1" '+f+'>◆</span><span class="corner corner_2" '+d+">◆</span></div>";if(!a("#floatCorner_"+g).size()){a("body").append(a(h))}this.corner=a("#floatCorner_"+g);return this},targetHold:function(){if(this.s.hoverHold){var d=parseInt(this.s.hideDelay,10)||200;if(this.target){this.target.hover(function(){c.flagDisplay=true},function(){if(c.timerHold){clearTimeout(c.timerHold)}c.flagDisplay=false;c.targetHold()})}c.timerHold=setTimeout(function(){c.displayDetect.call(c)},d)}else{this.displayDetect()}return this},loading:function(){this.target=a('<div class="float_loading"></div>');this.targetShow();this.target.removeData("width").removeData("height");return this},displayDetect:function(){if(!this.flagDisplay&&this.display){this.targetHide();this.timerHold=null}return this},targetShow:function(){c.cornerClear();this.display=true;this.container().setWidth().position();this.target.show();if(a.isFunction(this.s.showCall)){this.s.showCall.call(this.trigger,this.target)}return this},targetHide:function(){this.display=false;this.targetClear();this.cornerClear();if(a.isFunction(this.s.hideCall)){this.s.hideCall.call(this.trigger)}this.target=null;this.trigger=null;this.s={};this.targetProtect=false;return this},targetClear:function(){if(this.target){if(this.target.data("width")){this.target.removeData("width").removeData("height")}if(this.targetProtect){this.target.children().hide().appendTo(a("body"))}this.target.unbind().hide()}},cornerClear:function(){if(this.corner){this.corner.remove()}},target:null,trigger:null,s:{},cacheData:{},targetProtect:false};a.powerFloat={};a.powerFloat.hide=function(){c.targetHide()};var b={width:"auto",offsets:{x:0,y:0},zIndex:999,eventType:"hover",showDelay:0,hideDelay:0,hoverHold:true,hoverFollow:false,targetMode:"common",target:null,targetAttr:"rel",container:null,reverseSharp:false,position:"4-1",edgeAdjust:true,showCall:a.noop,hideCall:a.noop}})(jQuery);
/*
 * smartMenu.js 智能上下文菜单插件
 * http://www.zhangxinxu.com/
 *
 * Copyright 2011, zhangxinxu
 *
 * 2011-05-26 v1.0	编写
 * 2011-06-03 v1.1	修复func中this失准问题
 * 2011-10-10 v1.2  修复脚本放在<head>标签中层无法隐藏的问题
 * 2011-10-30 v1.3  修复IE6~7下二级菜单移到第二项隐藏的问题
 */
 
(function($) {
	var D = $(document).data("func", {});	
	$.smartMenu = $.noop;
	$.fn.smartMenu = function(data, options) {
		var B = $("body"), defaults = {
			name: "",
			offsetX: 2,
			offsetY: 2,
			textLimit: 6,
			beforeShow: $.noop,
			afterShow: $.noop
		};
		var params = $.extend(defaults, options || {});
		
		var htmlCreateMenu = function(datum) {
			var dataMenu = datum || data, nameMenu = datum? Math.random().toString(): params.name, htmlMenu = "", htmlCorner = "", clKey = "smart_menu_";
			if ($.isArray(dataMenu) && dataMenu.length) {
				htmlMenu = '<div id="smartMenu_'+ nameMenu +'" class="'+ clKey +'box">' +
								'<div class="'+ clKey +'body">' +
									'<ul class="'+ clKey +'ul">';
									
				$.each(dataMenu, function(i, arr) {
					if (i) {
						htmlMenu = htmlMenu + '<li class="'+ clKey +'li_separate">&nbsp;</li>';	
					}
					if ($.isArray(arr)) {
						$.each(arr, function(j, obj) {
							var text = obj.text, htmlMenuLi = "", strTitle = "", rand = Math.random().toString().replace(".", "");
							if (text) {
								if (text.length > params.textLimit) {
									text = text.slice(0, params.textLimit)	+ "…";
									strTitle = ' title="'+ obj.text +'"';
								}
								if ($.isArray(obj.data) && obj.data.length) {
									htmlMenuLi = '<li class="'+ clKey +'li" data-hover="true">' + htmlCreateMenu(obj.data) +
										'<a href="javascript:" class="'+ clKey +'a"'+ strTitle +' data-key="'+ rand +'"><i class="'+ clKey +'triangle"></i>'+ text +'</a>' + 
									'</li>';
								} else {
									htmlMenuLi = '<li class="'+ clKey +'li">' +
										'<a href="javascript:" class="'+ clKey +'a"'+ strTitle +' data-key="'+ rand +'">'+ text +'</a>' + 
									'</li>';
								}
								
								htmlMenu += htmlMenuLi;
								
								var objFunc = D.data("func");
								objFunc[rand] = obj.func;
								D.data("func", objFunc);
							}
						});	
					}
				});
				
				htmlMenu = htmlMenu + '</ul>' +
									'</div>' +
								'</div>';
			}
			return htmlMenu;
		}, funSmartMenu = function() {
			var idKey = "#smartMenu_", clKey = "smart_menu_", jqueryMenu = $(idKey + params.name);
			if (!jqueryMenu.size()) {
				$("body").append(htmlCreateMenu());
				
				//事件
				$(idKey + params.name +" a").bind("click", function() {
					var key = $(this).attr("data-key"),
						callback = D.data("func")[key];
					if ($.isFunction(callback)) {
						callback.call(D.data("trigger"));	
					}
					$.smartMenu.remove();
					return false;
				});
				$(idKey + params.name +" li").each(function() {
					var isHover = $(this).attr("data-hover"), clHover = clKey + "li_hover";
					
					$(this).hover(function() {
						var jqueryHover = $(this).siblings("." + clHover);
						jqueryHover.removeClass(clHover).children("."+ clKey +"box").hide();
						jqueryHover.children("."+ clKey +"a").removeClass(clKey +"a_hover");
						
						if (isHover) {					
							$(this).addClass(clHover).children("."+ clKey +"box").show();
							$(this).children("."+ clKey +"a").addClass(clKey +"a_hover");	
						}
						
					});
					
				});
				return $(idKey + params.name);
			} 
			return jqueryMenu;
		};
		
		$(this).each(function() {
			this.oncontextmenu = function(e) {
				//回调
				if ($.isFunction(params.beforeShow)) {
					params.beforeShow.call(this);	
				}
				e = e || window.event;
				//阻止冒泡
				e.cancelBubble = true;
				if (e.stopPropagation) {
					e.stopPropagation();
				}
				//隐藏当前上下文菜单，确保页面上一次只有一个上下文菜单
				$.smartMenu.remove();
				var st = D.scrollTop();
				var jqueryMenu = funSmartMenu();
				if (jqueryMenu) {
					jqueryMenu.css({
						display: "block",
						left: e.clientX + params.offsetX,
						top: e.clientY + st + params.offsetY
					});
					D.data("target", jqueryMenu);
					D.data("trigger", this);
					//回调
					if ($.isFunction(params.afterShow)) {
						params.afterShow.call(this);	
					}
					return false;
				}
			};
		});
		if (!B.data("bind")) {
			B.bind("click", $.smartMenu.hide).data("bind", true);
		}
	};
	$.extend($.smartMenu, {
		hide: function() {
			var target = D.data("target");
			if (target && target.css("display") === "block") {
				target.hide();
			}		
		},
		removeevent: function (event) {
		    var target = D.data("target");
		    if (target) {
		        target.remove();
		        if ($.isFunction(event)) {
		            event.call(this);
		        }
		    }
		},
		remove: function() {
			var target = D.data("target");
			if (target) {
				target.remove();
			}
		}
	});
})(jQuery);
/**
 * Author:       THINK
 * Create:       2018年02月14日 上午9:38:09
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 */
// 印管家项目JS核心工具包
// FrameWork基本信息
// console.log(navigator.userAgent);
var Helper = {};
Helper.author = "zjt";
Helper.version = "1.0 Beta";
Helper.cdate = "2016-01-01";
Helper.iswindow = /window/i.test(navigator.userAgent);
Helper.isie = /msie|rv/i.test(navigator.userAgent);
Helper.isff = /firefox/i.test(navigator.userAgent);
Helper.isop = /opera/i.test(navigator.userAgent);
Helper.ischrome = /Chrome/i.test(navigator.userAgent);
Helper.ismozilla = /Mozilla/i.test(navigator.userAgent);
Helper.browser = {
	name : Helper.isie ? "IE" : Helper.isff ? "Firefox" : Helper.isop ? "Opear" : Helper.ischrome ? "Chrome" : Helper.ismozilla ? "Mozilla " : "other",
	version : navigator.userAgent.match(/(?:rv|msie|firefox|opera|chrome|mozilla)[:| |\/]([\d\.]+)/i)[1]
};
Helper.isie6 = Helper.isie && Helper.browser.version == "6.0";

// 项目根路径
Helper.basePath = "";

/**==============================================
 *  扩展String
 * ============================================== **/
// 删除两边的空格
String.prototype.trim = function()
{
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
// 删除左边的空格
String.prototype.ltrim = function()
{
	return this.replace(/(^\s*)/g, "");
};
// 删除右边的空格
String.prototype.rtrim = function()
{
	return this.replace(/(\s*$)/g, "");
};
// 删除右边的空格
String.prototype.toBoolean = function()
{
	if (this.trim() == 'true')
	{
		return true;
	} else
	{
		return false;
	}
};
// 从头检测
String.prototype.startWith = function(str)
{
	var reg = new RegExp("^" + str);
	return reg.test(this);
};
// 从尾检测
String.prototype.endWith = function(str)
{
	var reg = new RegExp(str + "$");
	return reg.test(this);
};

/**==============================================
 *  扩展Date
 * ============================================== **/
// 格式化时间功能,例：yyyy-MM-dd hh:mm:ss:SSS
Date.prototype.format = function(format)
{
	if (!format)
	{
		format = "yyyy-MM-dd hh:mm:ss";
	}
	var _oDatePre = {
		"y+" : this.getFullYear(),// Year
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"S+" : this.getMilliseconds()
	// millisecond
	};
	for ( var _s in _oDatePre)
	{
		if (new RegExp("(" + _s + ")").test(format))
		{
			var _sValue = _oDatePre[_s];
			var _sLength = RegExp.$1.length;
			var _sFormatValue = "";
			if (_sValue.toString().length < _sLength)
			{
				for (var i = 0; i < _sLength - _sValue.toString().length; i += 1)
				{
					_sFormatValue += "0";
				}
			}
			_sFormatValue += _sValue.toString();
			format = format.replace(RegExp.$1, _sFormatValue);
		}
	}
	return format;
};
// 日期增加函数
Date.prototype.add = function(type, num)
{
	switch (type)
	{
	case "h":
		return new Date(Date.parse(this) + (3600000 * num));
	case "d":
		return new Date(Date.parse(this) + (86400000 * (num + 1)));
	case "w":
		return new Date(Date.parse(this) + ((86400000 * 7) * num) + 86400000);
	case "m":
		return new Date(this.getFullYear(), (this.getMonth()) + num, this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds());
	case "y":
		return new Date((this.getFullYear() + num), this.getMonth(), this.getDate(), this.getHours(), this.getMinutes(), this.getSeconds());
	}
};

/**==============================================
 *  扩展Number
 * ============================================== **/
Number.prototype.add = function(arg)
{
	return Helper.math.add(this, arg);
};
Number.prototype.subtr = function(arg)
{
	return Helper.math.subtr(this, arg);
};
Number.prototype.mul = function(arg)
{
	return Helper.math.mul(this, arg);
};
Number.prototype.div = function(arg)
{
	return Helper.math.div(this, arg);
};
Number.prototype.trunc = function()
{
	return Helper.math.trunc(this);
};
Number.prototype.ceil = function()
{
	return Helper.math.ceil(this);
};
Number.prototype.floor = function()
{
	return Helper.math.floor(this);
};
Number.prototype.round = function()
{
	return Helper.math.round(this);
};
Number.prototype.tomoney = function()
{
	return Helper.math.money(this);
};
Number.prototype.roundFixed = function(arg)
{
	return Helper.math.roundFixed(this, arg);
};

/**==============================================
 *  扩展Array
 * ============================================== **/
// 查找元素在数组中的位置
Array.prototype.indexOf = function(e)
{
	for (var i = 0, j; j = this[i]; i++)
	{
		if (j == e)
		{
			return i;
		}
	}
	return -1;
};
// 扩展数组对象，查询元素是否存在
Array.prototype.contains = function(oObj)
{
	var _THIS = this;
	for (var i = 0; i < _THIS.length; i += 1)
	{
		if (_THIS[i] === oObj)
		{
			return true;
		}
	}
	return false;
};
Array.prototype.containCount = function(oObj)
{
	var _THIS = this;
	var count = 0;
	for (var i = 0; i < _THIS.length; i += 1)
	{
		if (_THIS[i] === oObj)
		{
			count += 1;
		}
	}
	return count;
};
Array.prototype.copy = function()
{
	var arrs = [];
	for (var i = 0; i < this.length; i += 1)
	{
		arrs.push(this[i]);
	}
	return arrs;
};
Array.prototype.sum = function()
{
	var count = Number(0);
	for (var i = 0; i < this.length; i++)
	{
		count = count.add(Number(this[i]));
	}
	return count;
};
/*
 * 方法:Array.removeByValue(value) 功能:删除数组中第一个值与value相同的元素. 参数:value元素的值.
 * 返回:在原数组上修改数组.
 */
Array.prototype.removeByValue = function(value)
{
	for (var i = 0; i < this.length; i++)
	{
		if (this[i] == value)
		{
			this.remove(i);
			return true;
		}
	}
	return true;
};
/*
 * 方法:Array.baoremove(dx) 功能:删除数组元素. 参数:dx为元素的下标. 返回:在原数组上修改数组.
 */
Array.prototype.remove = function(dx)
{
	if (isNaN(dx) || dx > this.length)
	{
		return false;
	}
	this.splice(dx, 1);
	return true;
};
// 一维数组的排序
// type 参数
// 0 字母顺序（默认）
// 1 大小 比较适合数字数组排序
// 2 拼音 适合中文数组
// 3 乱序 有些时候要故意打乱顺序，呵呵
// 4 带搜索 str 为要搜索的字符串 匹配的元素排在前面
Array.prototype.sortBy = function(type, str)
{
	switch (type)
	{
	case 0:
		this.sort();
		break;
	case 1:
		this.sort(function(a, b)
		{
			return a - b;
		});
		break;
	case 2:
		this.sort(function(a, b)
		{
			return a.localeCompare(b)
		});
		break;
	case 3:
		this.sort(function()
		{
			return Math.random() > 0.5 ? -1 : 1;
		});
		break;
	case 4:
		this.sort(function(a, b)
		{
			return a.indexOf(str) == -1 ? 1 : -1;
		});
		break;
	default:
		this.sort();
	}
};
// 数字操作
Helper.math = {

	// 加法函数,arg1加上arg2的精确结果
	add : function(arg1, arg2)
	{
		var r1, r2, m;
		try
		{
			r1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
			r1 = 0
		}
		try
		{
			r2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
			r2 = 0
		}
		m = Math.pow(10, Math.max(r1, r2))
		return (arg1 * m + arg2 * m) / m
	},
	// 减法函数，用来得到精确的减法结果
	subtr : function(arg1, arg2)
	{
		var r1, r2, m, n;
		try
		{
			r1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
			r1 = 0
		}
		try
		{
			r2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
			r2 = 0
		}
		m = Math.pow(10, Math.max(r1, r2));
		// last modify by deeka
		// 动态控制精度长度
		n = (r1 >= r2) ? r1 : r2;
		return Number(((arg1 * m - arg2 * m) / m).toFixed(n));
	},
	// 乘法函数，用来得到精确的乘法结果
	// 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
	// 调用：accMul(arg1,arg2)
	// 返回值：arg1乘以arg2的精确结果
	mul : function(arg1, arg2)
	{
		var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
		try
		{
			m += s1.split(".")[1].length
		} catch (e)
		{
		}
		try
		{
			m += s2.split(".")[1].length
		} catch (e)
		{
		}
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
	},
	// 除法函数，用来得到精确的除法结果
	// 说明：javascript的除法结果会有误差，在两个浮点数相除的时候会比较明显。这个函数返回较为精确的除法结果。
	// 调用：accDiv(arg1,arg2)
	// 返回值：arg1除以arg2的精确结果
	div : function(arg1, arg2)
	{
		var t1 = 0, t2 = 0, r1, r2;
		try
		{
			t1 = arg1.toString().split(".")[1].length
		} catch (e)
		{
		}
		try
		{
			t2 = arg2.toString().split(".")[1].length
		} catch (e)
		{
		}
		with (Math)
		{
			r1 = Number(arg1.toString().replace(".", ""))
			r2 = Number(arg2.toString().replace(".", ""))
			return (r1 / r2) * pow(10, t2 - t1);
		}
	},
	// 丢弃小数部分
	trunc : function(arg)
	{
		return parseInt(arg);

	},
	// 向上取整
	ceil : function(arg)
	{
		return Math.ceil(arg);
	},
	// 向下取整
	floor : function(arg)
	{
		return Math.floor(arg);
	},
	// 四舍五入
	round : function(arg)
	{
		return Math.round(arg);
	},
	// 转换成money
	money : function(arg)
	{
		if (arg.toString().indexOf(".") > 0)
		{
			return Number(Number(arg).toFixed(2));
		} else
		{
			return Number(arg);
		}
	},
	// 优化toFixed，直接使用toFixed会有部分小数四舍五入错误
	// arg1=当前数值 ，arg2=保留小数位
	roundFixed : function(arg1, arg2)
	{
		if (arg2 == null)
		{
			arg2 = 0;
		}
		return ((Number(arg1) * Math.pow(10, arg2)).round() / Math.pow(10, arg2)).toFixed(arg2);
	}
}
// 转换操作
Helper.Conver = {

};
// 继承机制(该继承方式只会继承父类中的原型，不会继承父类自己本身的属性和方法)
Helper.inherits = function(fSubclass, fSuperclass)
{
	var F = function()
	{
	};
	F.prototype = fSuperclass.prototype;
	fSubclass.prototype = new F();
	fSubclass.prototype.constructor = fSuperclass;// 子类中保存父类的信息,主要用于子类原型重写父类原型方法后,可以调用到父类原型方法
	fSubclass.prototype.selfConstructor = fSubclass;// 保存自己类信息
};

Helper.Index = function()
{
	var index = 1000;
	this.getIndex = function()
	{
		return index += 1;
	};
};

Helper.Map = function()
{
	this.obj = {};
	this.prefix = "imap_key_prefix_";
	this.length = 0;
};
Helper.Map.prototype.put = function(key, value)
{
	if (!this.hasKey(key))
	{
		this.length += 1;
	}
	this.obj[this.prefix + key] = value;
};
Helper.Map.prototype.get = function(key)
{
	return this.obj[this.prefix + key];
};
Helper.Map.prototype.remove = function(key)
{
	if (this.hasKey(key))
	{
		var _oValue = this.get(key);
		delete this.obj[this.prefix + key];
		this.length -= 1;
		return _oValue;
	}
};
Helper.Map.prototype.clear = function()
{
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo))
		{
			delete this.obj[fo];
		}
	}
	this.length = 0;
};
Helper.Map.prototype.hasKey = function(key)
{
	var flag = this.obj.hasOwnProperty(this.prefix + key);
	return flag;
};
Helper.Map.prototype.size = function()
{
	return this.length;
};
Helper.Map.prototype.values = function()
{
	var _aArrays = [];
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo) && fo.substring(0, this.prefix.length).toLowerCase() === this.prefix)
		{
			_aArrays.push(this.obj[fo]);
		}
	}
	return _aArrays;
};
Helper.Map.prototype.keys = function()
{
	var _aArrays = [];
	for ( var fo in this.obj)
	{
		if (this.obj.hasOwnProperty(fo) && fo.substring(0, this.prefix.length).toLowerCase() === this.prefix)
		{
			_aArrays.push(fo.substr(this.prefix.length));
		}
	}
	return _aArrays;
};

Helper.Timmer = function(p)
{
	this.milli = p.milli;// 执行间隔,毫秒数
	this.count = p.count || -1;// -1表示无限执行
	this.call = p.call;// 执行的函数
	this.timmer = null;
	this.doCount = 0;// 已经执行的次数
	this.isRun = false;// 是否正在执行中
	var THIS = this;
	this.start = function()
	{
		if (!THIS.isRun && THIS.doCount === 0)
		{
			THIS.isRun = true;
			THIS.timmer = setInterval(function()
			{
				if (THIS.count > 0 && THIS.doCount >= THIS.count)
				{
					THIS.stop();
					return;
				} else
				{
					p.call();
					THIS.doCount += 1;
				}
			}, THIS.milli);
		}
	};
	this.stop = function()
	{
		if (THIS.timmer)
		{
			clearInterval(THIS.timmer);
		}
		THIS.doCount = 0;
		THIS.isRun = false;
		THIS.timmer = null;
	};
};

/**
 * 缓存类，该类仅供页面查询缓存使用，可以设置缓存的过期时间
 */
Helper.BaseCache = function(p)
{
	p = p || {};
	this.maxCacheTimeMM = p.maxCacheTimeMM || -1;// 页面缓存最大毫秒数,小于0则缓存永远不过期
	this.cacheMap = new Helper.Map();
};
Helper.BaseCache.prototype.addCache = function(key, data)
{
	var obj = {
		value : data
	};
	if (this.maxCacheTimeMM > 0)
	{
		obj.timeMM = new Date().getTime();
	}
	this.cacheMap.put(key, obj);
};
Helper.BaseCache.prototype.removeCache = function(key)
{
	this.cacheMap.remove(key);
};
Helper.BaseCache.prototype.clearCache = function()
{
	this.cacheMap.clear();
};
Helper.BaseCache.prototype.getCache = function(key)
{
	var data = this.cacheMap.get(key);
	if (!data)
		return undefined;
	if (this.maxCacheTimeMM <= 0)
	{
		return data.value;
	} else
	{
		if (new Date().getTime() <= (data.timeMM + this.maxCacheTimeMM))
		{
			return data.value;
		} else
		{
			this.removeCache(key);
			return undefined;
		}
	}
};

// 公用消息显示方法
Helper.message = {
	open : function(o)
	{
		layer.open(o);
	},
	tips : function(msg, follow, options)
	{
		layer.tips(msg, follow, options || ({
			tips : [ 1, '#0FA6D8' ]
		// 还可配置颜色
		}))
	},
	alert : function(msg, fun)
	{
		layer.msg(msg, fun);
	},
	suc : function(msg)
	{
		layer.msg(msg, {
			icon : 1
		});
	},
	err : function(msg)
	{
		layer.msg(msg, {
			icon : 2
		});
	},
	ask : function(msg)
	{
		layer.msg(msg, {
			icon : 3
		});
	},
	lock : function(msg)
	{
		layer.msg(msg, {
			icon : 4
		});
	},
	warn : function(msg)
	{
		layer.msg(msg, {
			icon : 5
		});
	},
	fail : function(msg)
	{
		layer.msg(msg, {
			icon : 6
		});
	},
	confirm : function(text, yes, cancel)
	{
		layer.confirm(text, {
			icon : 3
		}, yes, cancel);
	},
	prompt : function(title, value, fun)
	{
		layer.prompt({
			title : title,
			value : value,
			formType : 2,
			yes : fun
		});
	},
	view : function(msg)
	{
		layer.alert(msg);
	}
}

Helper.popup = {
	/* 弹出层 */
	/*
	 * 参数解释： title 标题 url 请求的url id 需要操作的数据id w 弹出层宽度（缺省调默认值） h 弹出层高度（缺省调默认值）
	 */
	show : function(title, url, w, h, full)
	{
		if (title == null || title == '')
		{
			title = false;
		}
		;
		if (url == null || url == '')
		{
			url = "404.html";
		}
		;
		if (w == null || w == '')
		{
			w = 800;
		}
		;
		if (h == null || h == '')
		{
			h = ($(window).height() - 50);
		}
		;
		var o = layer.open({
			type : 2,
			area : [ w + 'px', h + 'px' ],
			fix : false, // 不固定
			maxmin : true,
			shade : [ 0.4, "#CECFD8" ],
			title : title,
			content : url
		});
		if (full)
		{
			layer.full(o);
		}
	},
	/* 关闭弹出框口 */
	close : function()
	{
		var index = parent.layer.getFrameIndex(window.name);
		parent.layer.close(index);
	}
}

/**
 * 基础ajax访问类，该类用于页面基础ajax，统一错误处理功能，FROM提交功能
 */
Helper.Remote = {
	// from 增加参数提交
	fromSubmit : function(form, params)
	{// form 表单ID. params ajax参数
		var pp = {
			error : function(XMLHttpRequest, textStatus, errorThrown)
			{
				layer.open({
					type : 1,
					title : "出错啦！",
					area : [ '95%', '95%' ],
					content : "<div id='layerError' style='color:red'>" + XMLHttpRequest.responseText + "</div>"
				});
			}
		};
		$.extend(pp, params);
		$(form).ajaxSubmit(pp);
	},
	// 简单POST
	post : function(url)
	{
		var async = false;
		var params = null;
		var success = null;
		var error = null;
		var len = arguments.length;
		if (len == 5)
		{// url,params,success,error,async
			params = arguments[1];
			success = arguments[2];
			error = arguments[3];
			async = arguments[4];
		} else if (len == 4)
		{// url,params,success,error
			params = arguments[1];
			success = arguments[2];
			error = arguments[3];
		} else if (len == 3)
		{
			if (typeof (arguments[1]) === "function")
			{// url,success,error
				success = arguments[1];
				error = arguments[2];
			} else
			{// url,params,success
				params = arguments[1];
				success = arguments[2];
			}
		} else if (len == 2)
		{
			if (typeof (arguments[1]) === "function")
			{// url,success
				success = arguments[1];
			} else
			{// url,params
				params = arguments[1];
			}
		}
		$.ajax({
			type : "post",
			url : url,
			data : params,
			async : async,// 取消异步
			dataType : "json",
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			},
			success : success,
			error : error
		});
	},
	getJson : function(url, params)
	{
		var result = null;
		$.ajax({
			type : "get",
			url : url,
			data : params,
			async : false,// 取消异步
			dataType : "json",
			success : function(data)
			{
				result = data;
			}
		});
		return result;
	},
	request : function(obj)
	{
		$.ajax({
			type : obj.method || "POST",
			url : obj.url,
			data : JSON.stringify(obj.data || {}),// 将form序列化成JSON字符串
			dataType : obj.dataType || "json",
			contentType : obj.contentType || 'application/json;charset=utf-8', // 设置请求头信息
			async : obj.async || true,// 默认异步请求
			success : obj.success,
			error : obj.error || (function(data)
			{
				Helper.message.warn("请求错误")
			}),
			beforeSend : function()
			{
				layer.load(1);
			},
			complete : function()
			{
				layer.closeAll('loading');
			}
		});
	}
};

// 公用工具方法
Helper.tools = {
	/* 生成一个min到max的随机整数 */
	getRandomNum : function(min, max)
	{
		return Math.round((max - min) * Math.random() + min);
	},

	combine : function(result, data, list, count, low)
	{
		if (count == 0)
		{
			result.push(list.copy());
		} else
		{
			for (var i = low; i < data.length; i += 1)
			{
				list.push(data[i]);
				Helper.tools.combine(result, data, list, count - 1, i + 1);
				list.pop();
			}
		}
	},
	/**
	 * method 获得文本框内的字符长度，1中文字符等于2长度
	 */
	getCharCount : function(str)
	{
		var sum = 0;
		for (var i = 0, len = str.length; i < len; i++)
		{
			if ((str.charCodeAt(i) >= 0) && (str.charCodeAt(i) <= 255))
			{
				sum = sum + 1;
			} else
			{
				sum = sum + 2;
			}
		}
		return sum;
	},
	getFormatMoney : function(num, n)
	{
		if (num && n)
		{
			num = parseFloat(num);
			num = String(num.toFixed(n));
			var re = /(-?\d+)(\d{3})/;
			while (re.test(num))
			{
				num = num.replace(re, "$1,$2");
			}
			return num;
		} else
		{
			return "0.00";
		}
	},
	// [1,2,2] 去掉重复[1,2]
	getUnsameNumber : function(codes)
	{
		var numbers = [];
		for (var i = 0; i < codes.length; i += 1)
		{
			if (!numbers.contains(codes[i]))
			{
				numbers.push(codes[i]);
			}
		}
		return numbers;
	},
	// 一位数组转化为二维数组
	changeArray : function(arr)
	{
		var targetArr = new Array();
		for (var i = 0; i < arr.length; i++)
		{
			targetArr[i] = new Array();
			targetArr[i][0] = arr[i];
		}
		return targetArr;
	},
	// 把1位的数字号码转换为2位
	convert : function(nCode)
	{
		if (nCode.toString().length === 1)
		{
			return "0" + nCode.toString();
		} else
		{
			return nCode.toString();
		}
	},
	// 冒泡排序
	sort : function(aArray)
	{
		if (!aArray)
			return;
		for (var i = 0; i <= aArray.length; i++)
		{
			for (var j = 0; j < aArray.length - i - 1; j += 1)
			{
				if (Helper.doms.parseInt(aArray[j], 10) > Helper.doms.parseInt(aArray[j + 1], 10))
				{
					var _temp = aArray[j];
					aArray[j] = aArray[j + 1];
					aArray[j + 1] = _temp;
				}
			}
		}
	},

	/**
	 * html标签转义
	 */
	htmlspecialchars : function(str)
	{
		var s = "";
		if (str.length == 0)
			return "";
		for (var i = 0; i < str.length; i++)
		{
			switch (str.substr(i, 1))
			{
			case "<":
				s += "&lt;";
				break;
			case ">":
				s += "&gt;";
				break;
			case "&":
				s += "&amp;";
				break;
			case " ":
				if (str.substr(i + 1, 1) == " ")
				{
					s += " &nbsp;";
					i++;
				} else
					s += " ";
				break;
			case "\"":
				s += "&quot;";
				break;
			case "\n":
				s += "";
				break;
			default:
				s += str.substr(i, 1);
				break;
			}
		}
	}

};

// dom操作
Helper.doms = {
	id : function(id, target)
	{
		if (id)
		{
			target = target || "self";
			return window[target].document.getElementById(id);
		} else
		{
			return undefined;
		}
	},
	getValueInt : function(id)
	{
		var value = Helper.id(id).value.trim();
		if (value === "")
		{
			return 0;
		} else
		{
			return Helper.doms.parseInt(value);
		}
	},
	getHtmlInt : function(id)
	{
		var value = $("#" + id).text().trim();
		if (value === "")
		{
			return 0;
		} else
		{
			return Helper.doms.parseInt(value);
		}
	},
	paramsHtml : function(item)
	{
		var _sValue = location.search.match(new RegExp("[\?\&]" + item + "=([^\&]*)(\&?)", "i"));
		return _sValue ? _sValue[1] : _sValue;
	},
	numberKeyupEmpty : function(dom, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
			}
			if (typeof (success) === 'function')
			{
				success(dom);
			}
		};
		dom.onblur = function()
		{
			if (typeof (success) === 'function')
			{
				success(dom);
			}
		};
	},
	numberKeyup : function(dom, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
				if (this.value === "0")
				{
					this.value = 1;
				} else if (this.value.trim() === "")
				{
					this.value = 1;
				}
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
		dom.onblur = function()
		{
			if (dom.value === "")
			{
				dom.value = 1;
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
	},
	numberKeyupMinMax : function(dom, min, max, success)
	{
		dom.onkeyup = function()
		{
			if (dom.value !== "")
			{
				dom.value = this.value.replace(/[^\d]/g, ""); // 清除“数字”以外的字符
				var _nValue = Helper.doms.parseInt(this.value, 10);
				if (_nValue < min)
				{
					// this.value = min;
				} else if (_nValue > max)
				{
					this.value = max;
				}
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			}
		};
		dom.onblur = function()
		{
			if (dom.value === "")
			{
				dom.value = min;
				if (typeof (success) === 'function')
				{
					success(dom);
				}
			} else
			{
				var _nValue = Helper.doms.parseInt(this.value, 10);
				if (_nValue < min)
				{
					dom.value = min;
					if (typeof (success) === 'function')
					{
						success(dom);
					}
				}
			}
		};
	},
	enterSub : function(domId, success)
	{
		Helper.id(domId).onkeydown = function(event)
		{
			event = (event == null) ? window.event : event;
			if (event.keyCode == 13)
			{
				success();
			}
		};
	},
	parseInt : function(value, radio)
	{
		if (value.toString().trim() === "")
		{
			return 0;
		} else
		{
			return parseInt(value, radio ? radio : 10);
		}
	},
	/* textarea 字数限制 */
	textarealength : function(obj, maxlength)
	{
		var v = $(obj).val();
		var l = v.length;
		if (l > maxlength)
		{
			v = v.substring(0, maxlength);
			$(obj).val(v);
		}
		$(obj).parent().find(".textarea-numberbar").text(v.length + "/" + maxlength);
	}
};

Helper.select = {

	/**
	 * 移动两个select中的元素
	 * 
	 * @param selectBoxId
	 *          减少元素的控件
	 * @param resultBoxId
	 *          添加元素的控件
	 */
	moveSelectItem : function(selectBoxId, resultBoxId)
	{
		/* 权限列表box */
		var __selectBox = Helper.id(selectBoxId);
		/* 结果列表box */
		var __resultBox = Helper.id(resultBoxId);
		if (__selectBox.selectedIndex == -1)
		{
			alert("请选择您要操作的数据！");
			return;
		}
		var __option_items_index = new Array();
		for (var i = 0; i < __selectBox.options.length; i++)
		{
			if (__selectBox.options[i].selected)
			{
				__option_items_index.push(i);
			}
		}
		/* 必须从数组末端往前端删除，因为删除select的一个选项时，它内部的索引会改变 */
		for (var i = __option_items_index.length - 1; i >= 0; i--)
		{
			var __item = __selectBox.options[__option_items_index[i]];
			Helper.select.addItems(__resultBox, __item.value, __item.innerHTML);
			Helper.select.removeItems(__selectBox, __option_items_index[i]);
		}
	},

	/**
	 * select控件添加选项
	 * 
	 * @param currentBox
	 *          当前控件对象
	 * @param value
	 *          控件值
	 * @param label
	 *          控件显示值
	 */
	addItems : function(currentBox, value, label, color)
	{
		var __option = document.createElement("option");
		__option.value = value;
		__option.innerHTML = label;
		if (color)
		{
			__option.style.color = color;
		}
		currentBox.appendChild(__option);
	},

	/**
	 * 删除select选项
	 * 
	 * @param currentBox
	 *          当前select控件
	 * @param index
	 *          删除的索引
	 */
	removeItems : function(currentBox, index)
	{
		currentBox.remove(index);
	},

	/**
	 * 删除selectbox所有option
	 * 
	 * @param currentBox
	 *          select控件对象
	 */
	removeAllItems : function(currentBox)
	{
		if (currentBox.options <= 0)
			return;
		for (var i = currentBox.options.length - 1; i >= 0; i--)
		{
			currentBox.remove(i);
		}
	}

};

// 字符校验
Helper.validata = {
	// 判断是否为null
	isNull : function(value)
	{
		if (value === undefined || value === null)
		{
			return true;
		}
		return false;
	},
	isNotNull : function(value)
	{
		return !Helper.isNull(value);
	},
	isEmpty : function(value)
	{
		if (Helper.isNull(value))
		{
			return true;
		} else if (typeof (value) == "string")
		{
			if (value.trim() === "" || value === "undefined" || value === "[]" || value === "{}")
			{
				return true;
			}
		} else if (typeof (value) === "object")
		{
			return value.length <= 0 ? true : false;
		} else
		{
			return false;
		}
	},
	isNotEmpty : function(value)
	{
		return !Helper.isEmpty(value);
	},
	getByteLength : function(text)
	{
		if (Helper.isNull(text))
		{
			return 0;
		}
		return text.replace(/[^\u0000-\u00ff]/g, "aa").length;
	},
	// 验证中文
	isChinese : function(obj)
	{
		r = /[^\u4E00-\u9FA5]/g;
		return !r.test(obj);
	},
	isUsername : function(obj)
	{
		r = /^[A-Za-z0-9\u4E00-\u9FA5]+$/;
		return r.test(obj);
	},
	// --身份证号码验证-支持新的带x身份证
	isIdCardNo : function(pId)
	{
		if (pId.length != 15 && pId.length != 18)
			return false;
		var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19" + pId.slice(6, 16);
		if (!/^\d+$/.test(Ai))
			return false;
		var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai.slice(12, 14);
		var d = new Date(yyyy, mm, dd), year = d.getFullYear(), mon = d.getMonth(), day = d.getDate(), now = new Date();
		if (year != yyyy || mon != mm || day != dd || d > now || !Helper.validata.isValidData(dd, mm, yyyy))
			return false;
		return Helper.validata.isIdcardCheckno(pId);
	},
	// 身份证最后一位验证码验证
	isIdcardCheckno : function(idNo)
	{
		if (idNo.length == 15)
			return true;
		else
		{
			var a = [];
			for (var i = 0; i < idNo.length - 1; i++)
			{
				a[a.length] = idNo.substring(i, i + 1);
			}
			var w = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]; //
			var sum = 0; // 和
			var model = 0; // 模
			var result; // 结果
			var map = [ 1 ]; // 映射
			// 对应替换
			map[0] = 1;
			map[1] = 0;
			map[2] = 'X';
			map[3] = 9;
			map[4] = 8;
			map[5] = 7;
			map[6] = 6;
			map[7] = 5;
			map[8] = 4;
			map[9] = 3;
			map[10] = 2;
			// 求和
			for (var i = 0; i < a.length; i++)
			{
				sum += w[i] * a[i];
			}
			// 取模
			model = sum % 11;
			// 对应替换
			result = map[model];
			if (idNo.substring(17, 18) != result)
				return false;
			else
				return true;
		}
	},
	isValidDate : function(day, month, year)
	{
		if (month == 1)
		{
			var leap = (year % 4 == 0 || (year % 100 == 0 && year % 400 == 0));
			if (day > 29 || (day == 29 && !leap))
			{
				return false;
			}
		}
		return true;
	},

	/**
	 * 判断是否年满18
	 */
	suiTableAge : function(pId)
	{
		var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19" + pId.slice(6, 16);
		var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai.slice(12, 14);
		var time = new Date();

		// 满足18岁的最低日期
		time.setFullYear(time.getFullYear() - 18);
		var now_ms = time.getTime();

		// 身份证日期
		time.setFullYear(yyyy, mm, dd);
		var idcard_ms = time.getTime();

		return (now_ms >= idcard_ms);
	},

	isMobile : function(m)
	{
		var pattern = /^1[0-9]{10}$/;
		return pattern.test(m);
	},

	isEmail : function(e)
	{
		var pattern = /^[_a-zA-Z0-9\-]+(\.[_a-zA-Z0-9\-]*)*@[a-zA-Z0-9\-]+([\.][a-zA-Z0-9\-]+)+$/;
		return pattern.test(e);
	},

	isInteger : function(s)
	{
		var exp = new RegExp(/^\d+$/);
		return exp.test(s);
	},

	isMoney : function(s)
	{
		var exp = new RegExp(/^\d+(.\d{2})?$/);
		return exp.test(s);
	},

	isTelephone : function(str)
	{
		var reg = /^([0-9]|[\-])+$/g;
		if (str.length < 7 || str.length > 18)
		{
			return false;
		} else
		{
			return reg.test(str);
		}
	},
	/**
	 * 
	 * 验证最大尺寸（xxxx*xxxx）
	 * @since 1.0, 2017年10月19日 下午6:52:15, think
	 */
	isSize : function(s)
	{
		var exp = new RegExp(/^[1-9][0-9]{0,5}\*[1-9][0-9]{0,5}$/);
		return exp.test(s)
	},

	/**
	 * 验证最小尺寸（可以填写为 0*0）
	 * @since 1.0, 2017年11月30日 上午9:23:44, zhengby
	 */
	isSizeMin : function(s)
	{
		var exp = new RegExp(/^[0-9]{0,5}\*[0-9]{0,5}$/);
		return exp.test(s)
	},

	/**
	 * 
	 * 验证材料尺寸（xxxx*xxxx）
	 * 材料尺寸支持 xxx,xxx*xxx,xxx*xxx*xxx
	 * @since 1.0, 2017年10月19日 下午6:52:15, think
	 */
	isMaterialSize : function(s)
	{
		var exp1 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?$/);
		var exp2 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/);
		var exp3 = new RegExp(/^[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?\*[0-9]+([.]{1}[0-9]{1,4})?$/);
		var result1 = exp1.test(s);
		var result2 = exp2.test(s);
		var result3 = exp3.test(s);

		return (result1 || result2 || result3);
	},

	validataUname : function(username)
	{
		var _sErrorMessage = undefined;
		var leng = Helper.validata.getByteLength(username);
		if (Helper.isEmpty(username))
		{
			_sErrorMessage = "用户名不能为空";
		} else if (!Helper.validata.isUsername(username))
		{
			_sErrorMessage = "用户名含有非法字符";
		} else if (leng < 3 || leng > 16)
		{
			_sErrorMessage = "用户名字符长度应为3-16之间";
		}
		return _sErrorMessage;
	},
	validataPass : function(password)
	{
		var _sErrorMessage = undefined;
		var leng = Helper.validata.getByteLength(password);
		if (password === "")
		{
			_sErrorMessage = "密码不能为空";
		} else if (leng < 6 || leng > 16)
		{
			_sErrorMessage = "密码字符长度应为6-16之间";
		}
		return _sErrorMessage;
	},
	validataEmail : function(email)
	{
		var _sErrorMessage = undefined;
		if (email === "")
		{
			_sErrorMessage = "邮箱地址不能为空";
		} else if (!Helper.validata.isEmail(email))
		{
			_sErrorMessage = "邮箱格式不正确";
		} else if (email.length > 50)
		{
			_sErrorMessage = "邮箱地址过长";
		}
		return _sErrorMessage;
	},
	validataMobile : function(mobile)
	{
		var _sErrorMessage = undefined;
		if (mobile === "")
		{
			_sErrorMessage = "手机号码不能为空";
		} else if (!Helper.validata.isMobile(mobile))
		{
			_sErrorMessage = "手机号码格式不正确";
		}
		return _sErrorMessage;
	}
};

/**
 * 验证字段
 */
Helper.validfield = {
	/**
	 * 
	 * 表单验证字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldText : function($field, message)
	{
		if (Helper.isEmpty($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},
	/**
	 * 
	 * 表单验证整型字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldIntegerText : function($field, message)
	{
		if (!Helper.validata.isInteger($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},
	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldSizeText : function($field, message)
	{
		if (!Helper.validata.isSize($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},

	/**
	 * 
	 * 表单验证字段并提示信息
	 * @param $field
	 * @param eqmsg
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldEqText : function($field, eqmsg, message)
	{
		if (eqmsg == $field.val())
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	},

	/**
	 * 
	 * 表单验证xxx*xxx字段并提示信息
	 * @param $field
	 * @param message
	 * @returns {Boolean}
	 * @since 1.0, 2017年10月26日 下午3:19:55, think
	 */
	validateFieldMaterialSizeText : function($field, message)
	{
		if (!Helper.validata.isMaterialSize($field.val()))
		{
			Helper.message.tips(message, $field);
			$field.focus();
			return false;
		}
		return true;
	}
};

// 系统基础类
Helper.basic = {
	infoArray : function(type, field, values)
	{
		var result = Helper.cache.getCache("infoArray_" + type + "_" + field + "_" + values);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/basicInfoList",
				data : {
					'type' : type,
					'field' : field,
					'values' : values
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("infoArray_" + type + "_" + field + "_" + values, result);
		}
		return result;
	},
	info : function(type, id)
	{
		var result = Helper.cache.getCache("info_" + type + "_" + id);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/basicInfo",
				data : {
					'type' : type,
					'id' : id
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("info_" + type + "_" + id, result);
		}
		return result;
	},
	// 获取汇率对象（currencyType：兑换币别）
	getExchangeRate : function(currencyType)
	{
		var result = Helper.cache.getCache("getExchangeRate_" + currencyType);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/getExchangeRate",
				data : {
					'currencyType' : currencyType
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("getExchangeRate_" + currencyType, result);
		}
		return result;
	},
	// 获取枚举对象文本
	getEnumText : function(className, name, property)
	{
		var result = Helper.cache.getCache("getEnumText_" + className + "_" + name + "_" + className);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/public/getEnumText",
				data : {
					'className' : className,
					'name' : name,
					'property' : property
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("getEnumText_" + className + "_" + name + "_" + className, result);
		}
		return result;
	},
	// 是否有权限
	hasPermission : function(permission)
	{
		var result = Helper.cache.getCache("hasPermission_" + permission);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/hasPermission",
				data : {
					'permission' : permission
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("hasPermission_" + permission, result);
		}
		return result;
	},
	// 是否有报价系统
	hasOfferPermission : function(offerType, permission)
	{
		var result = Helper.cache.getCache("hasOfferPermission_" + permission);
		if (!result)
		{
			$.ajax({
				type : "get",
				url : Helper.basePath + "/hasOfferPermission",
				data : {
					'offerType' : offerType,
					'permission' : permission
				},
				async : false,// 取消异步
				dataType : "json",
				success : function(data)
				{
					result = data;
				}
			});
			Helper.cache.addCache("hasOfferPermission_" + permission, result);
		}
		return result;
	},
};

Helper.i18n = {
	getMsg : function(id)
	{
		var $msg = $("#" + id).data("msg");
		if ($msg.trim() == "" || $msg.startWith("i18n.jsp."))
		{
			$msg = $("#" + id).data("dft");
		}
		return $msg;
	}
};

Helper.Date = {
	diff : function(sDate1, sDate2)
	{ // sDate1和sDate2是2002-12-18格式
		s2 = (new Date(sDate2).getTime());
		s1 = (new Date(sDate1.replace(/-/g, "/"))).getTime();
		var days = (s1) - (s2);
		var time = Math.floor(days / (1000 * 60 * 60 * 24)) + 1;
		return time;
	}
};


(function($)
{
	/* 加载打印模板 */
	window.templateItem = {};
	$.fn.loadTemplate = function(billType, dataUrl)
	{
		var url = Helper.basePath + '/template/listWhitTitle?billType=' + billType;
		var _this = this;
		Helper.request({
			url : url,
			success : function(data)
			{
				if (data.success)
				{
					var ul = '<ul class="dropdown-menu" role="menu">';
					$.each(data.obj, function(key, value)
					{
						ul += ' <li><a title="' + value + '" href="' + Helper.basePath + '/sys/template/print?url=' + dataUrl + '&templateId=' + key + '" target="_blank">' + value + '</a></li>';
					});
					ul += '</ul>';
					$(_this).find(".template_div").append(ul);
				} else
				{
					var ul = '<ul class="dropdown-menu" role="menu">';
					ul += '<li>暂时没有模板</li>';
					ul += '</ul>';
					$(_this).find(".template_div").append(ul);
				}

			}
		});
	};
	$.fn.select = function(url, params, callBack)
	{
		var _THIS = $(this);
		$.ajax({
			type : "get",
			url : Helper.basePath + url,
			data : params,
			async : true,
			dataType : "json",
			success : function(data)
			{
				_THIS.empty();
				$.each(data, function(index, item)
				{
					_THIS.append('<option value="' + item[1] + '">' + item[0] + '</option>');
				});
				callBack();
			}
		});
	};
	$.fn.formToJson = function()
	{
		var jsonData1 = {};
		var serializeArray = this.serializeArray();
		// 先转换成{"id": ["12","14"], "name": ["aaa","bbb"],
		// "pwd":["pwd1","pwd2"]}这种形式
		$(serializeArray).each(function()
		{
			if (jsonData1[this.name] !== undefined)
			{
				if ($.isArray(jsonData1[this.name]))
				{
					jsonData1[this.name].push(Helper.isEmpty(this.value) ? null : this.value);
				} else
				{
					jsonData1[this.name] = [ jsonData1[this.name], this.value ];
				}
			} else
			{
				jsonData1[this.name] = Helper.isEmpty(this.value) ? null : this.value;
			}
		});

		// console.log(jsonData1);
		// console.log(JSON.stringify(jsonData1));
		// 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name":
		// "bb", "pwd":"pwd2"}]的形式
		var vCount = 0;
		// 计算json内部的数组最大长度
		for ( var item in jsonData1)
		{
			var temp = jsonData1[item];
			if ($.isArray(temp))
			{// 数组拆分
				var itemNameArray = item.split(".");
				var subName = itemNameArray[0];
				var subProperty = itemNameArray[1];
				if (!jsonData1[subName])
				{
					jsonData1[subName] = [];
				}
				for (var i = 0; i < temp.length; i++)
				{
					if (!jsonData1[subName][i])
					{
						jsonData1[subName][i] = {};
					}
					jsonData1[subName][i][subProperty] = Helper.isEmpty(temp[i]) ? null : temp[i];
				}
				delete jsonData1[item];// 移除原有属性
			} else
			{
				if (item.split(".").length > 1)
				{
					var _itemNameArray = item.split(".");
					var _subName = _itemNameArray[0];
					var _subProperty = _itemNameArray[1];
					if (!jsonData1[_subName])
					{
						jsonData1[_subName] = [];
						jsonData1[_subName][0] = {};
					}
					jsonData1[_subName][0][_subProperty] = Helper.isEmpty(temp) ? null : temp;
					delete jsonData1[item];// 移除原有属性
				}
			}
		}
		return jsonData1;
	};
})(jQuery);

// 全局缓存对像
Helper.cache = new Helper.BaseCache();
// 简写
Helper.id = Helper.doms.id;
Helper.getValueInt = Helper.doms.getValueInt;
Helper.getHtmlInt = Helper.doms.getHtmlInt;
Helper.paramsHtml = Helper.doms.paramsHtml;
Helper.isNull = Helper.validata.isNull;
Helper.isNotNull = Helper.validata.isNotNull;
Helper.isEmpty = Helper.validata.isEmpty;
Helper.isNotEmpty = Helper.validata.isNotEmpty;
Helper.post = Helper.Remote.post;
Helper.request = Helper.Remote.request;

// 公共初始化
$(function()
{
	// 所有需要用到字数限制的对象 新增class名为 input-txt_onlymemos
	$(".input-txt_onlymemos").each(function()
	{
		Helper.doms.textarealength(this, 100)
	})
})
/*
 * =============标签栏js=============
 */
/* 定义全局变量num和hide_nav */
var num = 0, oUl = $("#min_title_list"), hide_nav = $("#tabNav");

/* 定义变量获取对象 */
function min_titleList()
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find("#min_title_list");
	var aLi = show_nav.find("li");
}

/* 获得左侧菜单栏标签，在右边tab栏判断添加tab */
function admin_tab(obj)
{
	if ($(obj).attr('_href'))
	{
		var bStop = false;
		var bStopIndex = 0;
		var _href = $(obj).attr('_href');
		var _titleName = $(obj).attr("data-title");
		var _refresh = $(obj).attr('refresh');
		var topWindow = $(window.parent.document);
		var show_navLi = topWindow.find("#min_title_list li");
		/* 遍历顶部整个li列表，判断是否存在左边菜单选项 */
		show_navLi.each(function()
		{
			if ($(this).find('span').attr("title") == _titleName)
			{
				bStop = true;
				bStopIndex = show_navLi.index($(this));
				return false;
			}
		});
		if (!bStop)
		{
			creatIframe(_href, _titleName, _refresh);
			min_titleList();
		}
		/* 给点击标签li赋active 显示对应标签的frame */
		else
		{
			show_navLi.removeClass("active").eq(bStopIndex).addClass("active");
			show_navLi.eq(bStopIndex).find('span').html(_titleName);
			var iframe_box = topWindow.find("#iframe_box");
			/* 火狐display:none元素获取不到兼容写法 */
			/*
			 * iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe").attr("src",
			 * _href);
			 */
			iframe_box.find(".show_iframe").eq(bStopIndex).show().find("iframe").attr("src", _href);
			iframe_box.find(".show_iframe").eq(bStopIndex).siblings(".show_iframe").hide();
		}
	}
}

/* 给标签栏添加新的标签 */
function creatIframe(href, titleName, refresh)
{
	var topWindow = $(window.parent.document);
	var show_nav = topWindow.find('#min_title_list');
	show_nav.find('li').removeClass("active");
	var iframe_box = topWindow.find('#iframe_box');
	/* 为标签栏创建新li标签 */
	show_nav.append('<li class="active left-border"  onselectstart="return false" ><span data-href="' + href + '" title="' + titleName + '">' + titleName + '</span><i></i><em class="icon icon-cross"></em></li>');
	var taballwidth = 0, $tabNav = topWindow.find(".acrossTab"), $tabNavWp = topWindow.find(".tabNav-wp"), $tabNavitem = topWindow.find(".acrossTab li");
	/* 判断如果不存在标签，则返回 */
	if (!$tabNav[0])
	{
		return
	}
	/* 遍历整个标签栏中的标签，计算标签总长度 */
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32));
	});
	$tabNav.width(taballwidth + 25);
	/* iframe */
	var iframeBox = iframe_box.find('.show_iframe');
	iframeBox.hide();
	/* 创建并添加新的iframe */
	iframe_box.append('<div class="show_iframe"><div class="loading"></div><iframe every-refresh="' + refresh + '" frameborder="0" src=' + href + '></iframe></div>');
	var showBox = iframe_box.find('.show_iframe:visible');// 找出iframe可见的父级div
	showBox.find('iframe').load(function()
	{
		showBox.find('.loading').hide();// 找出新添加的iframe，隐藏载入动画;
	});
	tabvisible();
}

/* 标签被遮挡处理,使新添标签左移，让用户可见 */
function tabvisible()
{
	var taballwidth = 0, count = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavitem_active = hide_nav.find(".acrossTab li.active"), $tabNavWp = hide_nav.find(".tabNav-wp");
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	/* 获取整个标签栏div的长度 */
	var w = $tabNavWp.width();
	/* 当标签总长度大于标签栏的总长度时，全部标签左移 */
	if (taballwidth > (w - 45))
	{
		var tabnum = Number((taballwidth - w + 70 + $tabNavitem_active.width()) * (-1));
		$tabNav.css('left', tabnum + 'px');
	}
}

/* 关闭最右tab显示上一个Iframe和tab,即当关闭最右边的li时,显示上一个li,iframe也显示对应的 */
function removeIframe(obj)
{
	var aCloseIndex = obj.index();
	var iframe_box = $("#iframe_box");
	if (aCloseIndex > 0)
	{
		obj.remove();
		$('#iframe_box').find('.show_iframe').eq(aCloseIndex).remove();
		num == 0 ? num = 0 : num--;
		if ($("#min_title_list li.active").size() == 0)
		{
			$("#min_title_list li").removeClass("active").eq(aCloseIndex - 1).addClass("active");
			iframe_box.find(".show_iframe").hide().eq(aCloseIndex - 1).show();
		}
		/* 标签栏长度发生改变时触发此函数 */
		tabNavallwidth();
	} else
	{
		return false;
	}

	/*
	 * var topWindow = $(window.parent.document); var aCloseIndex =
	 * $(obj).parent().index(); var tab = topWindow.find(".acrossTab li"); var
	 * iframe = topWindow.find('#iframe_box .show_iframe');
	 * tab.eq(aCloseIndex).remove(); iframe.eq(aCloseIndex).remove(); num == 0?num =
	 * 0:num--; if($("#min_title_list li.active").size()==0)
	 * {//如果没有已激活的则显示上一个iframe标签 tab.removeClass('active');
	 * tab.eq(i-1).addClass("active"); iframe.hide(); iframe.eq(i-1).show(); }
	 */
}

/* 获取顶部选项卡总长度,判断触发点击事件(左右翻标签) */
function tabNavallwidth()
{
	var taballwidth = 0, $tabNav = hide_nav.find(".acrossTab"), $tabNavWp = hide_nav.find(".tabNav-wp"), $tabNavitem = hide_nav.find(".acrossTab li"), $tabNavmore = hide_nav.find(".tab-swith-btn");
	if (!$tabNav[0])
	{
		return
	}
	$tabNavitem.each(function(index, element)
	{
		taballwidth += Number(parseFloat($(this).width() + 32))
	});
	var w = $tabNavWp.width();
	if (taballwidth > (w - 90))
	{
		/* 点击下翻按钮 */
		$('.next-tab').click(function()
		{
			num == oUl.find('li').length - 1 ? num = oUl.find('li').length - 1 : num++;//
			toNavPos();
		});
		/* 点击上翻按钮 */
		$('.prev-tab').click(function()
		{
			num == 0 ? num = 0 : num--;
			toNavPos();
		});
	} else
	{
		if ($tabNav.offset().left == 220)
		{
			$('.tab-swith-btn').off('click');
		}
	}
}

/* 停止所有动画标签动画并开始当前动画 */
function toNavPos()
{
	oUl.stop().animate({
		'left' : -num * 100
	}, 100);
}

/**
 * 页签右建事件
 */
function createMouseDownMenuData(obj)
{
	obj.unbind("mousedown");
	obj.one("mousedown", (function(e)
	{
		if (e.which == 3)
		{// 右键事件
			var _THIS = $(this);
			var SELF = "self";
			var OTHER = "other";
			var LEFT = "left";
			var RIGHT = "right";
			var ALL = "all";
			var opertionn = {
				name : "",
				offsetX : 2,
				offsetY : 2,
				textLimit : 10,
				beforeShow : $.noop,
				afterShow : $.noop
			};
			var refreshSelf = function()
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var _iframe = $('#iframe_box').find('.show_iframe').eq(liCurrIndex).find("iframe");
				_iframe[0].contentWindow.location.reload();
				// _iframe.attr('src', _iframe.attr('src'));
			}
			var closeByType = function(type)
			{
				var liCurrIndex = $("#min_title_list li").index(_THIS);// 当前页签的索引（从0开始）
				var liLength = $("#min_title_list li").length;
				switch (type)
				{
				case SELF:
					_THIS.remove();
					$('#iframe_box').find('.show_iframe').eq(liCurrIndex).remove();
					num == 0 ? num = 0 : num--;
					break;
				case OTHER:
					for (var i = 0; i < liLength; i++)
					{
						if (i != 0)
						{
							if (i < liCurrIndex)
							{
								$("#min_title_list li").eq(1).remove();
								$('#iframe_box').find('.show_iframe').eq(1).remove();
							} else if (i > liCurrIndex)
							{
								$("#min_title_list li").eq(2).remove();
								$('#iframe_box').find('.show_iframe').eq(2).remove();
							}
						}
					}
					num = 1;
					break;
				case LEFT:
					for (var k = 0; k < liCurrIndex; k++)
					{
						if (k != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
							num == 0 ? num = 0 : num--;
						}
					}
					break;
				case RIGHT:
					for (var x = liCurrIndex; x < liLength; x++)
					{
						$("#min_title_list li").eq(liCurrIndex + 1).remove();
						$('#iframe_box').find('.show_iframe').eq(liCurrIndex + 1).remove();
						num == 0 ? num = 0 : num--;
					}
					break;
				case ALL:
					for (var y = 0; y < liLength; y++)
					{
						if (y != 0)
						{
							$("#min_title_list li").eq(1).remove();
							$('#iframe_box').find('.show_iframe').eq(1).remove();
						}
					}
					num = 0;
					break;
				}

				if (!$("#min_title_list li").hasClass("active"))
				{
					$("#min_title_list li").removeClass("active").eq(-1).addClass("active");
					$("#iframe_box").find(".show_iframe").hide().eq(-1).show();
				}
				tabNavallwidth();
			}

			var liCurrIndex = $("#min_title_list li").index($(this));// 当前页签的索引（从0开始）
			var liLength = $("#min_title_list li").length;
			function createMenuData()
			{

				var refreshSelf_tab = {
					text : "刷新",
					func : function()
					{
						refreshSelf();
					}
				};
				var closeSelf_tab = {
					text : "关闭",
					func : function()
					{
						closeByType(SELF);
					}
				};
				var closeOther_tab = {
					text : "关闭其它",
					func : function()
					{
						closeByType(OTHER);
					}
				};
				var closeLeft_tab = {
					text : "关闭左侧",
					func : function()
					{
						closeByType(LEFT);
					}
				};
				var closeRight_tab = {
					text : "关闭右侧",
					func : function()
					{
						closeByType(RIGHT);
					}
				};
				var closeAll_tab = {
					text : "关闭全部",
					func : function()
					{
						closeByType(ALL);
					}
				};
				if (liCurrIndex == 0)
				{
					return [ [ refreshSelf_tab ] ];
				} else
				{
					if (liLength == 2)
					{// 只有自己
						return [ [ refreshSelf_tab ], [ closeSelf_tab ] ];
					} else if (liCurrIndex == 1)
					{// 自己是第一个节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else if (liCurrIndex > 1 && (liCurrIndex < liLength - 1))
					{// 自己是中间节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeOther_tab, closeLeft_tab, closeRight_tab ], [ closeAll_tab ] ];
					} else
					{// 自己是末节点
						return [ [ refreshSelf_tab ], [ closeSelf_tab, closeLeft_tab ], [ closeAll_tab ] ];
					}
					return null;
				}

			}
			;
			_THIS.smartMenu(createMenuData(), opertionn);

		}
	}));
}

// 关闭当前标签并显示对应标题的页签
function closeTabAndJump(active_title, url)
{
	// 查找当前业签
	var _li_index = $(window.parent.document).find("#min_title_list").find("li.active").index();
	if (url)
	{
		admin_tab($("<a _href='" + url + "' data-title='" + active_title + "' />"));
	} else
	{
		// 显示被激活的业签
		$(window.parent.document).find("#min_title_list").find("li span[title='" + active_title + "']").parent().click();
	}
	// 关闭当前业签
	$(window.parent.document).find("#min_title_list").find("li:eq(" + _li_index + ") em").click();
}
// 图片缩放方法
function imgShow(outerdiv, innerdiv, bigimg, _this)
{
	var src = _this.attr("src");// 获取当前点击的pimg元素中的src属性
	$(bigimg).attr("src", src);// 设置#bigimg元素的src属性

	/* 获取当前点击图片的真实大小，并显示弹出层及大图 */
	$("<img/>").attr("src", src).load(function()
	{
		var windowW = $(window).width();// 获取当前窗口宽度
		var windowH = $(window).height();// 获取当前窗口高度
		var realWidth = this.width;// 获取图片真实宽度
		var realHeight = this.height;// 获取图片真实高度
		var imgWidth, imgHeight;
		var scale = 0.8;// 缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放

		if (realHeight > windowH * scale)
		{// 判断图片高度
			imgHeight = windowH * scale;// 如大于窗口高度，图片高度进行缩放
			imgWidth = imgHeight / realHeight * realWidth;// 等比例缩放宽度
			if (imgWidth > windowW * scale)
			{// 如宽度扔大于窗口宽度
				imgWidth = windowW * scale;// 再对宽度进行缩放
			}
		} else if (realWidth > windowW * scale)
		{// 如图片高度合适，判断图片宽度
			imgWidth = windowW * scale;// 如大于窗口宽度，图片宽度进行缩放
			imgHeight = imgWidth / realWidth * realHeight;// 等比例缩放高度
		} else
		{// 如果图片真实高度和宽度都符合要求，高宽不变
			imgWidth = realWidth;
			imgHeight = realHeight;
		}
		$(bigimg).css("width", imgWidth);// 以最终的宽度对图片缩放

		var w = (windowW - imgWidth) / 2;// 计算图片与窗口左边距
		var h = (windowH - imgHeight) / 2;// 计算图片与窗口上边距
		$(innerdiv).css({
			"top" : h,
			"left" : w
		});// 设置#innerdiv的top和left属性
		$(outerdiv).fadeIn("fast");// 淡入显示#outerdiv及.pimg
	});

	$(outerdiv).click(function()
	{// 再次点击淡出消失弹出层
		$(this).fadeOut("fast");
	});
}
// 在单据模块中点击单据编号，可以直接打开单据编号对应的单据模块的查看界面
function jumpTo(url, title)
{
	admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"));
}

/**
 * 根据单号转换url
 * @since 1.0, 2017年12月29日 上午9:08:20, zhengby
 */
function billNoTransToUrl(billNo)
{
	if (billNo == null || billNo == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 生产工单
	if (/^MO/g.test(billNo))
	{
		url = Helper.basePath + '/produce/work/toView/' + billNo;
		title = "生产工单";
	}
	// 销售订单
	else if (/^SO/g.test(billNo))
	{
		url = Helper.basePath + '/sale/order/view/' + billNo;
		title = "销售订单";
	}
	// 采购订单
	else if (/^PO/g.test(billNo))
	{
		url = Helper.basePath + '/purch/order/view/' + billNo;
		title = "采购订单";
	}
	// 采购退货单
	else if (/^PR/g.test(billNo))
	{
		url = Helper.basePath + '/purch/refund/view/' + billNo;
		title = "采购退货单";
	}
	// 采购入库单
	else if (/^PN/g.test(billNo))
	{
		url = Helper.basePath + '/purch/stock/view/' + billNo;
		title = "采购入库单";
	}
	// 发外加工单
	else if (/^OP/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/process/toView/' + billNo;
		title = "发外加工单";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/no/' + billNo;
		title = "报价单";
	}
	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 根据源单单号+id转换url
 * @since 1.0, 2017年12月29日 上午10:22:13, zhengby
 */
function idTransToUrl(id, billNo)
{
	if (id == null || id == "")
	{
		return;
	}
	var url = "";
	var title = "";
	// 销售退货单
	if (/^IR/g.test(billNo))
	{
		url = Helper.basePath + '/sale/return/view/' + id;
		title = "销售退货单";
	}
	// 销售送货单
	else if (/^IV/g.test(billNo))
	{
		url = Helper.basePath + '/sale/deliver/view/' + id;
		title = "销售送货单";
	}
	// 发外到货单
	else if (/^OA/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/arrive/view/' + id;
		title = "发外到货单";
	}
	// 发外退货单
	else if (/^OR[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/return/view/' + id;
		title = "发外退货单";
	}
	// 发外对账单
	else if (/^OC/g.test(billNo))
	{
		url = Helper.basePath + '/outsource/reconcil/view/' + id;
		title = "发外对账";
	}
	// 采购对账单
	else if (/^PK/g.test(billNo))
	{
		url = Helper.basePath + '/purch/reconcil/view/' + id;
		title = "采购对账";
	}
	// 销售对账单
	else if (/^SK/g.test(billNo))
	{
		url = Helper.basePath + '/sale/reconcil/view/' + id;
		title = "销售对账";
	}
	// 报价单
	else if (/^QU/g.test(billNo))
	{
		url = Helper.basePath + '/offer/view/' + id;
		title = "报价单";
	}
	// 产量上报
	else if (/^DY/g.test(billNo))
	{
		url = Helper.basePath + '/produce/report/view/' + id;
		title = "产量上报";
	}
	// 生产领料
	else if (/^MR/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/take/view/' + id;
		title = "生产领料";
	}
	// 生产补料
	else if (/^SM[0-9]/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/supplement/view/' + id;
		title = "生产补料";
	}
	// 生产退料
	else if (/^RM/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/return/view/' + id;
		title = "生产退料";
	}
	// 材料其他入库
	else if (/^SMI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherin/view/' + id;
		title = "材料其他入库";
	}
	// 材料其他出库
	else if (/^SMO/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/otherout/view/' + id;
		title = "材料其他出库";
	}
	// 材料库存调整
	else if (/^MA/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/adjust/view/' + id;
		title = "材料库存调整";
	}
	// 材料库存调拨
	else if (/^MT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/transfer/view/' + id;
		title = "材料库存调拨";
	}
	// 材料库存盘点
	else if (/^MI/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/inventory/view/' + id;
		title = "材料库存盘点";
	}
	// 材料分切
	else if (/^CT/g.test(billNo))
	{
		url = Helper.basePath + '/stockmaterial/split/view/' + id;
		title = "材料分切";
	}
	// 成品入库
	else if (/^IS/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/in/view/' + id;
		title = "成品入库";
	}
	// 成品其它入库
	else if (/^SPI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherin/view/' + id;
		title = "成品其它入库";
	}
	// 成品其它出库
	else if (/^SPO/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/otherout/view/' + id;
		title = "成品其它出库";
	}
	// 成品库存调整
	else if (/^PA/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/adjust/view/' + id;
		title = "成品库存调整";
	}
	// 成品库存调拨
	else if (/^PT/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/transfer/view/' + id;
		title = "成品库存调拨";
	}
	// 成品库存盘点
	else if (/^PI/g.test(billNo))
	{
		url = Helper.basePath + '/stockproduct/inventory/view/' + id;
		title = "成品库存盘点";
	}
	// 付款单
	else if (/^RV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/payment/view/' + id;
		title = "付款单";
	}
	// 收款单
	else if (/^RC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/receive/view/' + id;
		title = "收款单";
	}
	// 付款核销单
	else if (/^WRV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffPayment/view/' + id;
		title = "付款核销单";
	}
	// 收款核销单
	else if (/^WRC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/writeoffReceive/view/' + id;
		title = "收款核销单";
	}
	// 其它付款单
	else if (/^OV/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherPayment/view/' + id;
		title = "其它付款单";
	}
	// 其它收款单
	else if (/^ORC/g.test(billNo))
	{
		url = Helper.basePath + '/finance/otherReceive/view/' + id;
		title = "其它收款单";
	}
	// 材料期初单
	else if (/^BM/g.test(billNo))
	{
		url = Helper.basePath + '/begin/material/view/' + id;
		title = "材料期初单";
	}
	// 产品期初单
	else if (/^BP/g.test(billNo))
	{
		url = Helper.basePath + '/begin/product/view/' + id;
		title = "产品期初单";
	}
	// 代工单
	else if (/^EO/g.test(billNo))
	{
		url = Helper.basePath + '/oem/order/view/' + id;
		title = "代工单";
	}
	// 代工送货单
	else if (/^ED/g.test(billNo))
	{
		url = Helper.basePath + '/oem/deliver/view/' + id;
		title = "代工送货";
	}
	// 代工退货单
	else if (/^ER/g.test(billNo))
	{
		url = Helper.basePath + '/oem/return/view/' + id;
		title = "代工退货";
	}
	// 代工对账单
	else if (/^EC/g.test(billNo))
	{
		url = Helper.basePath + '/oem/reconcil/view/' + id;
		title = "代工对账";
	}
	// 客户期初
	else if (/^BC/g.test(billNo))
	{
		url = Helper.basePath + '/begin/customer/view/' + id;
		title = "客户期初";
	} 
	// 供应商期初
	else if (/^BS/g.test(billNo))
	{
		url = Helper.basePath + '/begin/supplier/view/' + id;
		title = "供应商期初";
	}
	// 财务调整单
	else if (/^FA/g.test(billNo))
	{
		url = Helper.basePath + '/finance/adjust/view/' + id;
		title = "财务调整单";
	} else
	{
		return billNo;
	}

	return '<a class="jump-to" title="点击链接" onclick="jumpTo(&quot;' + url + '&quot;,&quot;' + title + '&quot;)">' + billNo + '</a>';
}

/**
 * 表格列拖动的公共方法，依赖jQuery插件与colResizable插件
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var table_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	jQuery_$.colResizable({disable: true});		// 先废除
	jQuery_$.colResizable({
		minWidth: params.minWidth,				// 一个单元格最小宽度，默认15
		resizeMode: params.resizeMode,		// 默认'fit'
		onResize: params.resize						// 拖动后触发的方法
	})
}

/**
 * bootstrap_table表格列拖动方法
 * @since 1.0, 2018年10月16日 上午9:54:22, zhengxchn@163.com
 */
var bootstrapTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	params.resize = function(){
		jQuery_$.bootstrapTable('resetView');					// 每次拖动后重置bootstrapTable表格，用于重置表格的高度和宽度
	}
	table_ColDrag(jQuery_$, params);
}

/**
 * 为某一个标签下的所有未能列拖动的table标签添加列拖动效果，
 * 注意:
 *     该方法是筛选class不为"JColResizer"的Table标签
 * @since 1.0, 2018年10月18日 下午16:34:07, zhengxchn@163.com
 */
var allTable_ColDrag = function(jQuery_$, params){
	if (jQuery_$ == undefined || jQuery_$ == null)
	{
		return false;
	}
	if (params == undefined|| params == null)
	{
		params = {};
	}
	
	var tagName = jQuery_$.get(0).tagName;						// 检查当前的标签是否是Table标签
	if (tagName == "TABLE")
	{
		var coldrag = jQuery_$.attr("coldrag");
		if (coldrag != "false" && !jQuery_$.is(":hidden") && !jQuery_$.hasClass("JColResizer"))
		{
			// table没有coldrag属性，不是隐藏标签， class不包含JColResizer
			table_ColDrag(jQuery_$, params);
		}
	}
	
	var tables = jQuery_$.find("table");						// 查询该标签下的所有table
	$.each(tables, function(index, item)
	{
		var coldrag = $(item).attr("coldrag");
		if (coldrag == "false" || $(item).is(":hidden") || $(item).hasClass("JColResizer"))
		{
			// table有coldrag属性或是隐藏标签或包含JColResizer
			return true;
		}
		table_ColDrag($(item), params);
	});
}

/**
 * 默认开启表格列拖动方法
 * 适用于普通table和id为"bootTable"的bootstrap_table表格）
 * @since 1.0, 2018年10月16日 下午14:37:22, zhengxchn@163.com
 * */
$(function()
{
	var tables = $("body").find("table");			// 寻找页面上的table
	$.each(tables, function(index, item)
	{
		if ($(item).is(":hidden"))							// 隐藏table，跳过
		{
			return true;
		}
		
		var coldrag = $(item).attr("coldrag");	// coldrag用于关闭列拖动，在table标签加上coldrag="false"
		if (coldrag == "false")
		{
			return true;
		}
		
		var id = $(item).attr("id");						// 获取table的id属性
		if (id == "bootTable")									// table是bootstrap_table
		{
			$("#" + id).on('load-success.bs.table', function()			// bootstrap_table加载完后触发
			{
				bootstrapTable_ColDrag($("#" + id));
			})
			return true;
		}
		
		table_ColDrag($(item));																		// 普通table
	});
})
/* 处理键盘事件 */
function doKey(e)
{
	var ev = e || window.event; // 获取event对象
	var obj = ev.target || ev.srcElement; // 获取事件源
	var t = obj.type || obj.getAttribute('type'); // 获取事件源类型
	if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
	{
		return false;
	}
}
// 禁止后退键 作用于Firefox、Opera
document.onkeypress = doKey;
// 禁止后退键 作用于IE、Chrome
document.onkeydown = doKey;

$(function()
{
	/* 加载菜单图片 */
	function LoadMenuImg()
	{
		var menuIMG = $(".menu_img");
		$(".item").hover(function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			//$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + "_h.png");
			$(this).find(".menu_arrow").show();
		}, function()
		{
			var menuTitle = $(this).find(".item-tit").text()
			//$(this).find(".menu_img").attr("src", Helper.staticPath + "/layout/images/menu/" + menuTitle + ".png");
			$(this).find(".menu_arrow").hide();
		})
	}
	LoadMenuImg();

	/* 按时间问候 */
	function timeGreeting()
	{
		var dd = new Date();
		var st = $('.timeGreeting');
		var hour = dd.getHours();// 获取当前时
		if (hour > 0 && hour <= 6)
		{
			st.html("夜猫子，该休息了！");
		} else if (hour > 6 && hour <= 8)
		{
			st.html("上午好！ ");
		} else if (hour > 8 && hour <= 11)
		{
			st.html("早上好！ ");
		} else if (hour > 11 && hour <= 13)
		{
			st.html("中午好！ ");
		} else if (hour > 13 && hour <= 17)
		{
			st.html("下午好！ ");
		} else if (hour > 17 && hour <= 18)
		{
			st.html("傍晚好！ ");
		} else if (hour > 18 && hour <= 24)
		{
			st.html("晚上好！ ");
		}
	}
	timeGreeting();
	/* 浏览器窗口大小改变时-改变菜单图标大小 */
	window.onresize = function()
	{
		changeSize();
	}
	/* 页面加载时随浏览器窗口大小-改变菜单图标大小 */
	changeSize();
	function changeSize()
	{
		/*
		 * var h = document.documentElement.clientHeight;//获取页面可见高度 alert(h) if(h<615 &&
		 * h>500){ var num = parseInt((615-h)/7); $('.item
		 * i').css({"width":(38-num)+"px","height":(38-num)+"px"}); } else if(h>615 ||
		 * h==615){ $('.item i').css({"width":"30px","height":"30px"}); }
		 */
	}
	/* 二级菜单 */
	function subMenu()
	{
		var submenu = $(".submenu");
		var length = submenu.length;
		for (var i = 0; i < length; i++)
		{
			var menu = submenu.eq(i);
			switch (menu.data("title"))
			{
			case "销售管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "生产管理":
			{
				menu.css({
					"width" : "240px"
				});
				break;
			}
			case "采购管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "发外管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "库存管理":
			{
				menu.css({
					"width" : "630px"
				});
				break;
			}
			case "财务管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			case "基础设置":
			{
				menu.css({
					"width" : "580px"
				});
				break;
			}
			case "系统管理":
			{
				menu.css({
					"width" : "330px"
				});
				break;
			}
			case "代工管理":
			{
				menu.css({
					"width" : "440px"
				});
				break;
			}
			default:
			{
				menu.css({
					"width" : "330px"
				});
			}
			}
		}
		if (length <= 3)
		{
			for (var i = 0; i <= length; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
		} else if (length > 3 && length <= 6)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < length; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
		} else if (length > 6 && length <= 11)
		{
			for (var i = 0; i < 3; i++)
			{
				var menu = submenu.eq(i);
				menu.css({
					"top" : "0"
				});
			}
			for (var i = 3; i < 7; i++)
			{
				var height = submenu.eq(i).height();
				var menu = submenu.eq(i);
				if (height < 180)
				{
					menu.css({
						"bottom" : "0"
					});
				} else if (height >= 180)
				{
					menu.css({
						"bottom" : -1 * 0.5 * height + "px"
					});
				}
			}
			submenu.eq(7).css({
				"bottom" : "0"
			});
			submenu.eq(8).css({
				"bottom" : "0"
			});
			submenu.eq(9).css({
				"bottom" : "0"
			});
			submenu.eq(10).css({
				"bottom" : "0"
			});
		}

		/* 移除最后字段的竖线和下边框虚线 */
		$(".submenu").find("dl:last").css("border-bottom", "none");
		$(".submenu_item").find("dd:last").find("span").remove();
	}
	subMenu();

	/* 初始化导航栏菜单 */
	$(".links_list li").each(function()
	{
		var boxWidth = $("#" + $(this).attr("src")).innerWidth() * (-0.5) + 10;
		$(this).powerFloat({
			eventType : "hover",
			targetAttr : "src",
			reverseSharp : true,
			offsets : {
				x : boxWidth,
				y : -8
			},
			container : $(this).children(".box_container"),
		});
	})
	/* 悬浮显示子菜单控制 */
	$(".nav.menu-list .item").hover(function()
	{
		$(this).children(".submenu").fadeIn(250);
	}, function()
	{
		$(this).children(".submenu").fadeOut(10);
	})
	/* 广告跑马灯 */
	$(".marquee").css({
		"left" : $(".main_title").width() + 100,
		"right" : $(".topnav_right").width()
	}).marquee({
		speed : 40,
		gap : 500,
		delayBeforeStart : 1000,
		direction : 'left',
		// duplicated: true,这条会影响IE无限滚动
		pauseOnHover : true
	});
	/* 在线提问 */
	$("#online_ask").on("click", function(e)
	{
		var url = Helper.basePath + "/sys/service/system_notice";
		var title = "服务支持";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 新手指南 */
	$("#new_guide").on("click", function(e)
	{
		var url = Helper.basePath + "/guide/begin_guide";
		var title = "新手指南";
		admin_tab($("<a _href='" + url + "' data-title='" + title + "' />"))
	})
	/* 关于 */
	$("#about").on("click", function(e)
	{
		$('.about').stop(true, true);
		$('.about_mask').show();
		$('.about').animate({
			top : "50%",
			opacity : "0.9"
		}, 300);
		$(".clo_icon").click(function()
		{
			$('.about').stop(true, true);
			$(".about").animate({
				top : "30%",
				opacity : "0"
			}, 300);
			$('.about_mask').hide();
		})
	})
	/* 换肤图标 */
	$("#change_skin").click(function()
	{
		$(".skin_list").toggle();
	})
	/* 切换 */
	$(".skin_list>ul>li>span").click(function()
	{
		var cssType = $(this).siblings("input").val(), skin_default_i = $(".skin_default"), skin_brown_i = $(".skin_brown"), skin_blue_i = $(".skin_blue");
		if (cssType == "default")
		{
			skin_default_i.addClass("active");
			skin_blue_i.removeClass("active");
			skin_brown_i.removeClass("active");
		} else if (cssType == "brown")
		{
			skin_brown_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_blue_i.removeClass("active");
		} else if (cssType == "blue")
		{
			skin_blue_i.addClass("active");
			skin_default_i.removeClass("active");
			skin_brown_i.removeClass("active");
		}
		$("#topskin").attr("href", Helper.ctxHYUI + '/themes/' + cssType + '/style.css?v=' + Helper.v);
		$.cookie('ygj_skin', $(this).siblings("input").val(), {
			expires : 365
		});
	})
	/* 点击关闭单个标签 */
	$("#min_title_list").on("click", "li em", function()
	{
		removeIframe($(this).parent());
	});
	/* 给li双击事件，关闭当前标签 */
	$(document).on("dblclick", "#min_title_list li", function()
	{
		removeIframe($(this));
	});
	/* 单击选项卡单个tab（切换tab） */
	$(document).on("click", "#min_title_list li", function()
	{
		var bStopIndex = $(this).index();
		var iframe_box = $("#iframe_box");
		$("#min_title_list li").removeClass("active").eq(bStopIndex).addClass("active");
		var _iframe = iframe_box.find(".show_iframe").hide().eq(bStopIndex).show().find("iframe");
		if (_iframe.attr("every-refresh") == "true")
		{// 时时刷新
			_iframe.attr("src", _iframe.attr("src"));
		} else
		{
			_iframe.show();
		}
	});
	// 鼠标移动到选项卡事件（添加右键事件）
	$(document).on("mouseenter", "#min_title_list li", function()
	{
		createMouseDownMenuData($(this))
	});
	// 检查是否可以直接跳转到印刷家
	$(document).on("click", "#forward", function()
	{
		// 同意协议
		Helper.request({
			url : Helper.basePath + "/sys/user/getUser",
			error : function(request)
			{
				Helper.message.warn("服务器繁忙");
			},
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj.mobile == "" || data.obj.mobile == null)
					{
						Helper.message.view("账户未填写手机号，请联系公司管理员");
						return;
					} else
					{
						if($("#userIsSign").val() == 'NO' || $("#userIsSign").val() == '')
						{
							Helper.popup.show('用户协议', Helper.basePath + '/exterior/viewAgreement', '590', '520');
						} else
						{
							var newWin = window.open('', '我要采购');
							newWin.location.href = Helper.basePath + "/exterior/load";
						}
					}
				}
			}
		});
	})
	// 点击左侧菜单标签触发函数;
	$(".menu-list a").on("click", function(e)
	{
		admin_tab(this);
		tabNavallwidth();
	});
	queryExpire();
	getVersionInfo();
	// 检查是否可以继续购买
	$("#buy").on("click", function()
	{
		$.ajax({
			type : "POST",
			async : false,
			url : Helper.basePath + "/sys/buy/isPay",
			dataType : "json",
			contentType : 'application/json;charset=utf-8', // 设置请求头信息
			success : function(data)
			{
				if (data.success)
				{
					if (data.obj)
					{
						window.open(Helper.basePath + "/pay/step1/choose");
					} else
					{
						Helper.message.warn("请先支付或取消订单，在进行操作!");
						return;
					}
				} else
				{
					// 如果登陆超时,只要重新调用location,则会自动跳到登陆页面
					window.location = "/";
				}
			}
		});
	});
})

// 跳转到印刷家
function forwardYSJ(callback)
{
	Helper.request({
		url : Helper.basePath + "/exterior/forwardYSJ",
		error : function(request)
		{
			Helper.message.warn("服务器繁忙");
		},
		success : function(data)
		{
			// 响应
			if (data.success)
			{
				callback.call(this);
			} else
			{
				// 用户未绑定印刷家
				if (data.message == "")
				{
					Helper.popup.show('用户协议', Helper.basePath + '/exterior/viewAgreement', '590', '520');
				} else
				{// 系统错误
					Helper.message.warn(data.message);
				}
			}
		}
	})
}

function queryExpire()
{
	$.ajax({
		type : "POST",
		url : Helper.basePath + "/queryExpire",
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		success : function(data)
		{
			if (data.message.split("_")[0] == 0)
			{
			} else if (data.message.split("_")[0] == 1)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家还有" + data.message.split("_")[1] + "天就到期了，为了避免产品过期影响您的使用，请尽快联系深圳华印进行续费，联系电话：400-800-8755，QQ2880157226");
			} else if (data.message.split("_")[0] == 2)
			{
				Helper.message.view("尊敬的印管家客户，您试用的印管家还有" + data.message.split("_")[1] + "天就到期了，如果您需要购买正式版，请联系深圳华印，联系电话：400-800-8755");
			} else if (data.message.split("_")[0] == 3)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家已过期，请联系深圳华印，联系电话：400-800-8755");
			} else if (data.message.split("_")[0] == 4)
			{
				Helper.message.view("尊敬的印管家客户，您的印管家试用已过期，如果您需要购买正式版，请联系深圳华印，联系电话：400-800-8755");
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}
// 查询版本更新通知
function getVersionInfo()
{
	$.ajax({
		type : "POST",
		url : Helper.basePath + "/getVersionInfo",
		dataType : "json",
		contentType : 'application/json;charset=utf-8', // 设置请求头信息
		success : function(data)
		{
			if (data.obj)
			{
				layer.open({
					title : '升级公告',
					content : data.obj.content
				});
			}
		},
		error : function(data)
		{
			// console.log(data);
		}
	});
}

/**
 * 同意印刷家协议
 */
function ysjAgreement()
{
	$("#userIsSign").val("YES");
}
