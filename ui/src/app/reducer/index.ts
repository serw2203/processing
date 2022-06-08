import {combineReducers, Reducer} from 'redux';
import {Location} from "history";
import history from '../history';
import {connectRouter, RouterState, LocationChangeAction} from "connected-react-router";

export interface IStringHashMap<T> {
    [index: string]: T;
}

export interface IRouterLocation extends Location {
    query: IStringHashMap<string>;
}

export interface IRouterState extends RouterState {
    location: IRouterLocation;
}

const routerReducer = connectRouter(history) as Reducer<IRouterState, LocationChangeAction>;

const rootReducer = combineReducers({
    router: routerReducer
});

export default rootReducer;
