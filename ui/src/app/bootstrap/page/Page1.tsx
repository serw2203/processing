import * as React from 'react';
import {hot} from 'react-hot-loader/root';
import {wait} from '../../util';

class Page1 extends React.Component<{}, {}> {
    render() {
        //wait(2000);

        return <div>
            {'PAGE 1 XXXXXYZ'}
        </div>;
    }
}

export default hot(Page1);
