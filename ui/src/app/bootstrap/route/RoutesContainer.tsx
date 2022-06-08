import * as React from 'react';
import {Link, Route, Switch} from 'react-router-dom';
import {useSelector} from 'react-redux';
import * as Loadable from 'react-loadable';
import {wait} from '../../util';

const StartMenu: React.FC<{}> = () => {
    //wait(2000);

    return <>
        <div>
            <Link to={'/proc/page1'}>Страница 1</Link>
        </div>
        <div>
            <Link to={'/proc/page2'}>Страница 2</Link>
        </div>
    </>;
};

const Loader: React.FC<unknown> = () => {
    return <div>Loading chunk...</div>;
};

const Page1_ = Loadable({
    loader: () =>
        import(/* webpackChunkName: "page1" */ '../page/Page1'),
    loading: Loader
});

const Page2_ = Loadable({
    loader: () =>
        import(/* webpackChunkName: "page2" */ '../page/Page2'),
    loading: Loader
});


const Routes: React.FC = () => {
    return <>
        <Route exact path={'/proc/page1'} component={Page1_}/>
        <Route exact path={'/proc/page2'} component={Page2_}/>
    </>;
};

const RoutesContainer: React.FC<unknown> = () => {
    const location = useSelector<any, any>(state => state.router.location);

    return <Switch>
        <Route exact path={'/'} component={StartMenu} location={location}/>
        <Route path={'/proc'} component={Routes} location={location}/>
    </Switch>

};

export default RoutesContainer;

