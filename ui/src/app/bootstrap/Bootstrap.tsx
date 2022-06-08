import * as React from 'react';
import {Provider} from 'react-redux';
import {createStore, applyMiddleware} from 'redux';
import {createEpicMiddleware} from 'redux-observable';
import history from '../history';
import {routerMiddleware} from 'react-router-redux';
import {ConnectedRouter} from 'connected-react-router';
import reducer from '../reducer';
import {LayoutContainer} from './layout';
import rootEpic from './epic';

const epicMiddleware = createEpicMiddleware();

const store = createStore(reducer, {}, applyMiddleware(epicMiddleware, routerMiddleware(history)));

epicMiddleware.run(rootEpic);

// @ts-ignore
window.store = store;

export const Bootstrap: React.FC = () => (
    <Provider store={store}>
        <ConnectedRouter history={history}>
            <LayoutContainer/>
        </ConnectedRouter>
    </Provider>
);

