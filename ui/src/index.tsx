import 'whatwg-fetch';
import '@babel/polyfill';
import 'url-search-params-polyfill';

import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Bootstrap} from 'app';
import 'bootstrap/dist/css/bootstrap.min.css';

ReactDOM.render(<Bootstrap/>, document.getElementById('app'));
